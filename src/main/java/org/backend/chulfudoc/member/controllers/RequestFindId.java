package org.backend.chulfudoc.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestFindId (
        @NotBlank
        String name,

        @NotBlank
        @Email
        String email){}
