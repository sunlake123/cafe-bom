package com.kwy.cafebom.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class SignupMemberForm {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$])[A-Za-z\\d~!@#$]{8,11}$"
        , message = "비밀번호는 영어 대소문자, 숫자, 특수문자(~!@#$)를 포함한 8~11 자리로 입력해야 합니다.")
    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String password;

    @Pattern(regexp = "^[가-힣]*$", message = "닉네임은 한글로 입력해야 합니다.")
    @Size(min = 2, max = 6, message = "닉네임은 2~6 자리로 입력해야 합니다.")
    @NotBlank(message = "닉네임은 필수로 입력해야 합니다.")
    private String nickname;

    @Size(min = 11, max = 13, message = "전화번호는 11~13 자리로 입력해야 합니다.")
    @NotBlank(message = "전화번호는 필수로 입력해야 합니다.")
    private String phone;

    @Email(message = "이메일 형식을 확인해주세요.")
    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    private String email;
}