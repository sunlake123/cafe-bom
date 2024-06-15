package com.kwy.cafebom.common.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws ServletException, IOException {

        setUseForward(true);
        setDefaultFailureUrl("/login?error=true");
        request.setAttribute("errorMessage", "로그인에 실패하였습니다.");

        super.onAuthenticationFailure(request, response, exception);
    }
}
