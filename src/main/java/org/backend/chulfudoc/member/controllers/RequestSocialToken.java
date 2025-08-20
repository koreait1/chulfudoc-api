package org.backend.chulfudoc.member.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.backend.chulfudoc.member.constants.SocialChannel;

@Data
public class RequestSocialToken extends RequestToken{
    @NotBlank
    private SocialChannel channel;

    @NotBlank
    private String token;
}
