package org.backend.chulfudoc.hospital;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.backend.chulfudoc.hospital.entities.Hospital;
import org.backend.chulfudoc.hospital.repositories.HospitalRepository;
import org.junit.jupiter.api.Test;
import org.locationtech.proj4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@SpringBootTest
public class HospitalDataTransfer {

    @Autowired
    private HospitalRepository repository;

    @Test
    void process() throws Exception {
        CsvMapper mapper = new CsvMapper();

        try (FileInputStream fis = new FileInputStream("C:/hospital/ERPlusPlus.csv");
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr, 16000)) {
            int cnt = 0;
            String line = null;
            while((line = br.readLine()) != null) {
                cnt++;
                if (cnt == 1) continue;

                try {
                    if (line.endsWith(",")) {
                        line = line.substring(0, line.lastIndexOf(","));
                    }
                    line = line.replace("?,", "\",");

                    List<String> item = mapper.readValue(line, new TypeReference<>() {
                    });

                    String hospital = item.get(0);
                    String address = item.get(1);
                    String mobile = item.get(2);
                    double loc1 = Double.parseDouble(item.get(3));
                    double loc2 = Double.parseDouble(item.get(4));

                    double[] pos = transformTMToWGS84(loc1, loc2);

                    double lat = pos[0];
                    double lon = pos[1];

                    System.out.printf("hospital:%s, address:%s, mobile:%s, loc1: %s, loc2: %s%n", hospital, address, mobile, loc1, loc2);

                    Hospital hos = new Hospital();
                    hos.setName(hospital);
                    hos.setAddress(address);
                    hos.setMobile(mobile);
                    hos.setLat(lat);
                    hos.setLon(lon);

                    repository.saveAndFlush(hos);

                } catch (Exception e) {
                    System.out.println("---- 오류 -----");
                    System.out.println(line);
                    e.printStackTrace();
                }

            }
        }
    }


    private double[] transformTMToWGS84(double lat, double lon) {
        CRSFactory crsFactory = new CRSFactory();

        // WGS84 좌표계 (EPSG:4326) - 위도 경도
        CoordinateReferenceSystem crsWGS84 = crsFactory.createFromName("EPSG:4326");

        // TM 중부원점 좌표계 (EPSG:2097)
        CoordinateReferenceSystem crsTM = crsFactory.createFromParameters("EPSG:2097", "+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=bessel +units=m +no_defs +towgs84=-115.80,474.99,674.11,1.16,-2.31,-1.63,6.43");

        // CoordinateTransformFactory 생성
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

        // 좌표 변환 객체 생성
        //CoordinateTransform transform = ctFactory.createTransform(crsWGS84, crsTM);
        CoordinateTransform transform = ctFactory.createTransform(crsTM, crsWGS84);
        // 변환할 좌표 설정
        ProjCoordinate sourceCoordinate = new ProjCoordinate(lon, lat);
        ProjCoordinate targetCoordinate = new ProjCoordinate();

        // 좌표 변환 수행
        transform.transform(sourceCoordinate, targetCoordinate);

        return new double[]{targetCoordinate.y, targetCoordinate.x};
    }
}
