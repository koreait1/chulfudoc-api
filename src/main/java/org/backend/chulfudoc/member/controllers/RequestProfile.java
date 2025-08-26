package org.backend.chulfudoc.member.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestProfile {
    private String password;
    private String confirmPassword;
    @NotBlank
    private String name;
    @NotBlank
    private String mobile;
}
