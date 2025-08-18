package org.backend.chulfudoc.member.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.exceptions.BadRequestException;
import org.backend.chulfudoc.global.libs.Utils;
import org.backend.chulfudoc.member.entities.Member;
import org.backend.chulfudoc.member.jwt.TokenService;
import org.backend.chulfudoc.member.libs.MemberUtil;
import org.backend.chulfudoc.member.services.JoinService;
import org.backend.chulfudoc.member.validators.JoinValidator;
import org.backend.chulfudoc.member.validators.TokenValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@Tag(name="회원 API", description = "회원 가입, 인증 토큰 발급 기능 제공")
public class MemberController {

    private final JoinValidator joinValidator;
    private final JoinService joinService;

    private final TokenValidator tokenValidator;
    private final TokenService tokenService;

    private final MemberUtil memberUtil;
    private final Utils utils;

    @Operation(summary = "회원가입처리", method = "POST")
    @ApiResponse(responseCode = "201", description = "회원가입 성공시 201로 응답, 검증 실패시 400")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201로 응답
    public void join(@Valid @RequestBody RequestJoin form, Errors errors) {

        joinValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        joinService.process(form);
    }

    /**
     * 회원 계정(이메일, 비밀번호)으로 JWT 토큰 발급
     *
     * @return
     */
    @Operation(summary = "회원 인증 처리", description = "아이디와 비밀번호로 인증한 후 회원 전용 요청을 보낼수 있는 토큰(JWT)을 발급")
    @Parameters({
            @Parameter(name="userId", required = true, description = "아이디"),
            @Parameter(name="password", required = true, description = "비밀번호")
    })
    @ApiResponse(responseCode = "200", description = "인증 성공시 토큰(JWT)발급")
    @PostMapping("/token")
    public String token(@Valid @RequestBody RequestToken form, Errors errors) {

        tokenValidator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return tokenService.create(form.getUserId());
    }

//    @PreAuthorize("isAuthenticated()") // 로그인시에만 접근 가능
//    @GetMapping("/test1")
//    public void test1(Principal principal) { // 사용자 이름만 가져옴
//        System.out.println("principal:" + principal.getName());
//        System.out.println("로그인시 접근 가능 - test1()");
//    }

//    @PreAuthorize("isAuthenticated()") // 로그인시에만 접근 가능
//    @GetMapping("/test1")
//    public void test1(@AuthenticationPrincipal MemberInfo memberInfo) { // 사용자 정보를 가져옴
//        System.out.println("memberInfo:" + memberInfo);
//        System.out.println("로그인시 접근 가능 - test1()");
//    }

    /**
     * 로그인한 회원 정보 출력
     *
     * @return
     */
    @Operation(summary = "로그인 상태인 회원 정보를 조회", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping // GET /api/v1/memberg
    @PreAuthorize("isAuthenticated()")
    public Member myInfo() {
        return memberUtil.getMember();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/test2")
    public void test2() {
        System.out.println("관리자만 접근 가능 - test2()");
    }

}