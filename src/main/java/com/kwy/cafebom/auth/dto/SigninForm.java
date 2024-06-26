package com.kwy.cafebom.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

public class SigninForm {

    @Getter
    public static class Request {

        @Email(message = "이메일 형식을 확인해주세요.")
        private String email;

        @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
        private String password;
    }

    @Getter
    @Builder
    public static class Response {

        private String token;
    }
}