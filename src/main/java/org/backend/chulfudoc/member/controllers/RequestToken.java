package org.backend.chulfudoc.member.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestToken {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;

}
