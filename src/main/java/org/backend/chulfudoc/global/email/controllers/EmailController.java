package org.backend.chulfudoc.global.email.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.email.services.EmailVerifyService;
import org.backend.chulfudoc.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Tag(name="이메일 API", description = "이메일 인증번호 발급 및 검증")
public class EmailController {

    private final EmailVerifyService verifyService;

    @Operation(summary = "이메일 인증번호 발송", method = "GET")
    @ApiResponse(responseCode = "200", description = "이메일 발송 성공시 emailSuccess : true 반환")
    @Parameter(name = "email", required = true, description = "인증 번호를 받아볼 이메일")
    @GetMapping("/verify")
    public JSONData<Object> sendVerifyEmail(@RequestParam("email") String email) {
        JSONData<Object> data = new JSONData<>();

        boolean result = verifyService.sendCode(email);
        data.setEmailSuccess(result);
        if(result){
            data.setStatus(HttpStatus.OK);
        }else {
            data.setStatus(HttpStatus.BAD_REQUEST);
        }

        return data;
    }

    @Operation(summary = "이메일 인증번호 검증", method = "GET")
    @ApiResponse(responseCode = "200", description = "인증 성공시 emailSuccess : true 반환")
    @Parameter(name="authNum", required = true, description = "이메일 인증번호")
    @GetMapping("/check")
    public JSONData<Object> checkVerifiedEmail(@RequestParam("authNum") int authNum, @RequestParam("User-Hash") String userHash){
        JSONData<Object> data = new JSONData<>();

        boolean result = verifyService.check(userHash, authNum);
        data.setEmailSuccess(result);
        if(result){
            data.setStatus(HttpStatus.OK);
        }else {
            data.setStatus(HttpStatus.BAD_REQUEST);
        }

        return data;
    }
}
