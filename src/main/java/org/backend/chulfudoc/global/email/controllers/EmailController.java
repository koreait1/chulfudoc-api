package org.backend.chulfudoc.global.email.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.email.services.EmailVerifyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ApiResponse(description = "이메일 발송 여부 -> 성공 : 200 || 실패 : 400")
    @Parameter(name = "email", required = true, description = "인증 번호를 받아볼 이메일")
    @GetMapping("/verify")
    public ResponseEntity<Void> sendVerifyEmail(@RequestParam("email") String email) {

        boolean isValid = verifyService.sendCode(email);
        HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(status);
    }

    @Operation(summary = "이메일 인증번호 검증", method = "GET")
    @ApiResponse(description = "이메일 인증 성공 여부 -> 성공 : 200 || 실패 : 400")
    @Parameter(name="authNum", required = true, description = "이메일 인증번호")
    @GetMapping("/check")
    public ResponseEntity<Void> checkVerifiedEmail(@RequestParam("authNum") int authNum){

        boolean isValid = verifyService.check(authNum);
        HttpStatus status = isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(status);
    }
}
