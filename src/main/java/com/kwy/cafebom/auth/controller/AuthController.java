package com.kwy.cafebom.auth.controller;

import static com.kwy.cafebom.auth.dto.SigninDto.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.kwy.cafebom.auth.dto.SigninDto;
import com.kwy.cafebom.auth.dto.SigninDto.Request;
import com.kwy.cafebom.auth.dto.SigninForm;
import com.kwy.cafebom.auth.dto.SignupAdminForm;
import com.kwy.cafebom.auth.dto.SignupDto;
import com.kwy.cafebom.auth.dto.SignupMemberForm;
import com.kwy.cafebom.auth.service.impl.AuthService;
import com.kwy.cafebom.common.config.security.TokenProvider;
import com.kwy.cafebom.common.exception.CustomException;
import com.kwy.cafebom.common.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @Value("${admin.code}")
    private String ADMIN_CODE;

    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignup(
        @RequestBody @Valid SignupMemberForm signupMemberForm) {

        authService.signup(SignupDto.from(signupMemberForm));
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<Void> adminSignup(
        @RequestBody @Valid SignupAdminForm signupAdminForm) throws CustomException {

        if (!signupAdminForm.getAdminCode().equals(ADMIN_CODE)) {
            throw new CustomException(ErrorCode.ADMIN_CODE_NOT_MATCH);
        }
        authService.signup(signupAdminForm);
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninForm.Response> signin(
        @RequestBody @Valid SigninForm.Request signinForm
    ) {
        Response signinDto = authService.signin(Request.from(signinForm));
        String accessToken = tokenProvider.generateToken(
                signinDto.getMemberId(), signinDto.getEmail(), signinDto.getRole());
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        SigninForm.Response.builder()
                                .token(accessToken).build()
                );
    }
}
