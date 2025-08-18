package org.backend.chulfudoc.hospital.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.hospital.entities.Hospital;
import org.backend.chulfudoc.hospital.services.HospitalInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
@Tag(name="병원위치 정보 API", description = "서비스에 필요한 정보 전달(병원 위치)")
public class HospitalController {

    private final HospitalInfoService service;

    @Operation(summary = "병원 목록 조회", description = "DB내 담겨있는 모든 병원 정보 조회")
    @ApiResponse(responseCode = "200", description = "병원 정보 조회 시 활용")
    @GetMapping("/list")
    public List<Hospital> hospitalLocation(){

        List<Hospital> items = service.getList();
        return items;
    }

}
