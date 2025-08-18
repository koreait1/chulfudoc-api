package org.backend.chulfudoc.hospital;

import org.backend.chulfudoc.hospital.entities.Hospital;
import org.backend.chulfudoc.hospital.services.HospitalInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HospitalInfoServiceTest {

    @Autowired
    private HospitalInfoService service;

    @Test
    void test(){
        List<Hospital> items = service.getList();

        items.forEach(System.out::println);
    }
}
