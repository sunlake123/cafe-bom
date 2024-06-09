package com.kwy.cafebom.common.config.security;

import com.kwy.cafebom.auth.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String KEY_ROLE = "role";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 1일 (24시간)

    private final AuthService authService;

    @Value("${spring.jwt.secret}")
    private String secretKey;


}
