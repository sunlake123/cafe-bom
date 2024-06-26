package com.kwy.cafebom.auth.service.impl;

import static com.kwy.cafebom.common.config.security.Role.ROLE_ADMIN;
import static com.kwy.cafebom.common.config.security.Role.ROLE_USER;
import static com.kwy.cafebom.common.exception.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.kwy.cafebom.common.exception.ErrorCode.MEMBER_NOT_EXISTS;
import static com.kwy.cafebom.common.exception.ErrorCode.NICKNAME_ALREADY_EXISTS;
import static com.kwy.cafebom.common.exception.ErrorCode.PASSWORD_NOT_MATCH;

import com.kwy.cafebom.auth.dto.SigninDto;
import com.kwy.cafebom.auth.dto.SignupAdminForm;
import com.kwy.cafebom.auth.dto.SignupDto;
import com.kwy.cafebom.common.exception.CustomException;
import com.kwy.cafebom.front.member.domain.Member;
import com.kwy.cafebom.front.member.domain.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

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


    public SigninDto.Response signin(SigninDto.Request signinDto) {

        Member member = memberRepository.findByEmail(signinDto.getEmail())
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        if (!passwordEncoder.matches(signinDto.getPassword(), member.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }
        return SigninDto.Response.builder()
            .memberId(member.getId())
            .email(member.getEmail())
            .role(member.getRole())
            .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (member.getRole().toString().equals(ROLE_ADMIN.toString())) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (member.getRole().toString().equals(ROLE_USER.toString())) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
            .username(member.getEmail())
            .password(member.getPassword())
            .authorities(grantedAuthorities)
            .build();
    }
}
