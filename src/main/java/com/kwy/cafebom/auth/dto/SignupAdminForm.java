package com.kwy.cafebom.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupAdminForm {

    @NotBlank(message = "관리자 인증코드는 필수로 입력해야 합니다.")
    @Size(min = 6, max = 6, message = "인증코드는 숫자 6자리입니다.")
    private String adminCode;

    @NotBlank(message = "관리자 실명은 필수로 입력해야 합니다.")
    @Pattern(regexp = "^[가-힣]{2,4}$", message = "관리자 실명은 한글 2~4자리로 입력해야 합니다.")
    private String adminName;

    @Email(message = "이메일 형식을 확인해주세요.")
    @NotBlank(message = "이메일은 필수로 입력해야 합니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$])[A-Za-z\\d~!@#$]{8,11}$"
        , message = "비밀번호는 영어 대소문자, 숫자, 특수문자(~!@#$)를 포함한 8~11 자리로 입력해야 합니다.")
    @NotBlank(message = "비밀번호는 필수로 입력해야 합니다.")
    private String password;

    @Size(min = 11, max = 13, message = "전화번호는 11~13 자리로 입력해야 합니다.")
    @NotBlank(message = "전화번호는 필수로 입력해야 합니다.")
    private String phone;
}
