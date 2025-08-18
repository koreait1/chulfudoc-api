package org.backend.chulfudoc.hospital.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(length = 20)
    private String name;

    @Column(length = 100)
    private String address;

    @Column(length = 15)
    private String mobile;

    // 위도
    private double lat;
    // 경도
    private double lon;
}
