package org.backend.chulfudoc.member.controllers;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.backend.chulfudoc.member.constants.SocialChannel;

@Data
public class RequestJoin {

    private String gid;

    @NotBlank
    private String userId;

    private String password;
    private String confirmPassword;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String mobile;

    @AssertTrue
    private boolean termsAgree;

    private SocialChannel socialChannel;
    private String socialToken;

    private String authNum;
}
