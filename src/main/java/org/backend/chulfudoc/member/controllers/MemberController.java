package org.backend.chulfudoc.member.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.backend.chulfudoc.global.exceptions.BadRequestException;
import org.backend.chulfudoc.global.libs.Utils;
import org.backend.chulfudoc.member.entities.Member;
import org.backend.chulfudoc.member.jwt.TokenService;
import org.backend.chulfudoc.member.libs.MemberUtil;
import org.backend.chulfudoc.member.services.FindPwService;
import org.backend.chulfudoc.member.services.JoinService;
import org.backend.chulfudoc.member.services.ProfileUpdateService;
import org.backend.chulfudoc.member.validators.JoinValidator;
import org.backend.chulfudoc.member.validators.ProfileValidator;
import org.backend.chulfudoc.member.validators.TokenValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final ProfileValidator profileValidator;
    private final ProfileUpdateService profileUpdateService;

    private final MemberUtil memberUtil;
    private final Utils utils;

    private final HttpServletRequest request;

    private final FindPwService findPwService;

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
    @Operation(summary = "회원 인증 처리", description = "이메일과 비밀번호로 인증한 후 회원 전용 요청을 보낼수 있는 토큰(JWT)을 발급")
    @Parameters({
            @Parameter(name="userId", required = true, description = "아이디, 일반 로그인 시 필수"),
            @Parameter(name="password", required = true, description = "비밀번호, 일반 로그인시 필수"),
            @Parameter(name="socialChannel", required = true, description = "소셜 로그인 채널 구분, 소셜 로그인 시 필수"),
            @Parameter(name="socialToken", required = true, description = "소셜 로그인시 발급받은 회원 구분 값, 소셜 로그인시에만 필수")
    })
    @ApiResponse(responseCode = "200", description = "인증 성공시 토큰(JWT)발급")
    @PostMapping({"/token", "/social/token"})
    public String token(@Valid @RequestBody RequestToken form, Errors errors) {
        form.setSocial(request.getRequestURI().contains("/social"));

        tokenValidator.validate(form, errors);
        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return form.isSocial() ? tokenService.create(form.getSocialChannel(), form.getSocialToken()) : tokenService.create(form.getUserId());
    }

//    @PreAuthorize("isAuthenticated()") // 로그인시에만 접근 가능
//    @GetMapping("/test1")
//    public void test1(Principal principal) { // 사용자 이름만 가져옴
//        System.out.println("principal:" + principal.getName());
//        System.out.println("로그인시 접근 가능 - test1()");
//    }

    /**
     * 로그인한 회원 정보 출력
     *
     * @return
     */
    @Operation(summary = "로그인 상태인 회원 정보를 조회", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping // GET /api/v1/member
    public ResponseEntity<Member> myInfo() {

        return memberUtil.isLogin() ? ResponseEntity.ok(memberUtil.getMember()): ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "로그인한 회원의 회원정보를 수정 처리", method = "PATCH")
    @PatchMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public Member update(@Valid @RequestBody RequestProfile form, Errors errors) {

        profileValidator.validate(form, errors);

        if (errors.hasErrors())throw new BadRequestException(utils.getErrorMessages(errors));
        return profileUpdateService.process(form);
    }

    @Operation(summary = "로그인 상태인 회원 정보를 수정 처리", method = "PATCH")
    @PatchMapping("/update/PUUID")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Member updateAdmin(@Valid @RequestBody RequestProfile form, Errors errors) {

        return null;
    }

    /**
     * 비밀번호 찾기
     * @param errors
     * @return
     */
    @Operation(summary = "비밀번호 찾기", method = "GET", description = "userId + email 검증 후 메일로 임시 비밀번호 전송")
    @Parameters({
            @Parameter(name="userId", required = true, description = "아이디"),
            @Parameter(name="email", required = true, description = "회원가입 시 인증받은 이메일")
    })
    @ApiResponse(responseCode = "200", description = "처리 성공")
    @GetMapping("/findpw")
    public ResponseEntity<RequestFindPw> findPw(@RequestParam String userId, @RequestParam String email, Errors errors) {

        RequestFindPw form = new RequestFindPw(userId, email);
        System.out.println(form);
        findPwService.process(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("hasAnyAuthority('ADMIN')")
//    @GetMapping("/test2")
//    public void test2() {
//        System.out.println("관리자만 접근 가능 - test2()");
//    }

//    @PatchMapping("/update/{seq}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public Member updateAdmin() {
//
//    }
}