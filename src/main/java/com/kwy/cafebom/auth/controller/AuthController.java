package com.kwy.cafebom.auth.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.kwy.cafebom.auth.dto.SignupDto;
import com.kwy.cafebom.auth.dto.SignupMemberForm;
import com.kwy.cafebom.auth.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> memberSignup(
        @RequestBody @Valid SignupMemberForm signupMemberForm) {

        authService.signup(SignupDto.from(signupMemberForm));
        return ResponseEntity.status(CREATED).build();
    }
}
