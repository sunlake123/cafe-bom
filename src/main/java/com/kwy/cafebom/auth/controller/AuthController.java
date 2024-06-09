package com.kwy.cafebom.auth.controller;

import static org.springframework.http.HttpStatus.CREATED;

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
}
