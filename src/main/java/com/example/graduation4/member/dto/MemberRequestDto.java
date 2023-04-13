package com.example.graduation4.member.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class MemberRequestDto {

    @Getter
    @Setter
    public static class Register {

        @Pattern(regexp="[가-힣|a-z|A-Z|0-9]{3,30}",message="3~30길이의 알파벳, 한글, 숫자로 입력해주세요")
        @NotBlank(message="userId을 입력해주세요.")
        @NotNull
        private String userId;

        @Pattern(regexp="[가-힣|a-z|A-Z|0-9]{1,30}",message="1~30길이의 알파벳, 한글, 숫자로 입력해주세요")
        @NotBlank(message="username을 입력해주세요.")
        @NotNull
        private String username;

        @NotBlank(message="비밀번호를 입력해주세요")
        @Pattern(regexp = "[a-zA-Z!@#$%^&*-_]{6,20}",message="6~20길이의 알파벳과 숫자로 입력해주세요")
        @NotNull
        private String password;

        // @NotBlank(message="전화번호를 입력해주세요")
        @Pattern(regexp = "[0-9]{6,11}",message="- 없이 숫자만 입력해주세요")
        private String phoneNumber;

    }

    @Getter
    @Setter
    public static class Login {

        @Pattern(regexp="[가-힣|a-z|A-Z|0-9]{3,30}",message="3~30길이의 알파벳, 한글, 숫자로 입력해주세요")
        @NotBlank(message="userId을 입력해주세요.")
        @NotNull
        private String userId;

        @NotBlank(message="비밀번호를 입력해주세요")
        @Pattern(regexp = "[a-zA-Z!@#$%^&*-_]{6,20}",message="6~20길이의 알파벳과 숫자로 입력해주세요")
        @NotNull
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(userId, password);
        }

    }

    @Getter
    @Setter
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Logout {
        @NotEmpty(message = "잘못된 요청입니다.")
        private String accessToken;

        @NotEmpty(message = "잘못된 요청입니다.")
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class IdCheck {

        @Pattern(regexp="[가-힣|a-z|A-Z|0-9]{3,30}",message="3~30길이의 알파벳, 한글, 숫자로 입력해주세요")
        @NotBlank(message="userId을 입력해주세요.")
        @NotNull
        private String userId;

    }

    // Update
    @Getter
    @Setter
    public static class Update {

        @Pattern(regexp="[가-힣|a-z|A-Z|0-9]{3,30}",message="3~30길이의 알파벳, 한글, 숫자로 입력해주세요")
        @NotBlank(message="userId을 입력해주세요.")
        @NotNull
        private String userId;

        @Pattern(regexp = "[0-9]{6,11}",message="- 없이 숫자만 입력해주세요")
        private String phoneNumber;


    }


}
