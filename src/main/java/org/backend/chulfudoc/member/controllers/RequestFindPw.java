package org.backend.chulfudoc.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestFindPw(
        @NotBlank
        String userId,

        @NotBlank
        @Email
        String email
) {}
