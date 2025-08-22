package org.backend.chulfudoc.member.controllers;

import lombok.Data;
import org.backend.chulfudoc.member.constants.SocialChannel;

@Data
public class RequestToken {

    private boolean social;
    private String email;
    private String password;
    private SocialChannel socialChannel;
    private String socialToken;
}
