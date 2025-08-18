package org.backend.chulfudoc.hospital.services;

import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.hospital.entities.Hospital;
import org.backend.chulfudoc.hospital.repositories.HospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HospitalInfoService {

    private final HospitalRepository repository;

    public List<Hospital> getList(){

        return repository.findAll();
    }
}
