package com.kwy.cafebom.auth.service.impl;

import static com.kwy.cafebom.common.config.security.Role.ROLE_ADMIN;
import static com.kwy.cafebom.common.config.security.Role.ROLE_USER;
import static com.kwy.cafebom.common.exception.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.kwy.cafebom.common.exception.ErrorCode.NICKNAME_ALREADY_EXISTS;

import com.kwy.cafebom.auth.dto.SignupAdminForm;
import com.kwy.cafebom.auth.dto.SignupDto;
import com.kwy.cafebom.common.exception.CustomException;
import com.kwy.cafebom.front.member.domain.Member;
import com.kwy.cafebom.front.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public void signup(SignupDto signupDto) {

        if (memberRepository.findByNickname(signupDto.getNickname()).isPresent()) {
            throw new CustomException(NICKNAME_ALREADY_EXISTS);
        }
        if (memberRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new CustomException(EMAIL_ALREADY_EXISTS);
        }
        memberRepository.save(
            Member.from(signupDto, passwordEncoder.encode(signupDto.getPassword()), ROLE_USER));
    }

    public void signup(SignupAdminForm signupAdminForm) {
        SignupDto signupDto = SignupDto.from(signupAdminForm);

        memberRepository.findByNickname(signupAdminForm.getAdminName()).ifPresent(member -> {
            throw new CustomException(NICKNAME_ALREADY_EXISTS);
        });

        memberRepository.findByEmail(signupAdminForm.getEmail()).ifPresent(member -> {
            throw new CustomException(EMAIL_ALREADY_EXISTS);
        });

        memberRepository.save(
            Member.from(signupDto, passwordEncoder.encode(signupDto.getPassword()), ROLE_ADMIN));
    }


}
