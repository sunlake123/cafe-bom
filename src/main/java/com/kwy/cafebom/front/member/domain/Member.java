package com.kwy.cafebom.front.member.domain;

import com.kwy.cafebom.auth.dto.SignupDto;
import com.kwy.cafebom.common.BaseTimeEntity;
import com.kwy.cafebom.common.config.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String phone;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member from(SignupDto signupDto, String encodedPassword, Role role) {
        return Member.builder()
                .password(encodedPassword)
                .nickname(signupDto.getNickname())
                .phone(signupDto.getPhone())
                .email(signupDto.getEmail())
                .role(role)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(new String[]{String.valueOf(role)})
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
