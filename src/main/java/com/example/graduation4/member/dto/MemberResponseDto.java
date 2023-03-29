package com.example.graduation4.member.dto;

import com.example.graduation4.member.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberResponseDto {

    @Getter
    @Builder
    private static class MemberBody {

        private String result;
        private String userId;
        private String username;
        private String phone_number;
    }

    public ResponseEntity<?> memberSuccess(Member member) {

        MemberBody body = MemberBody.builder()
                .result("회원가입 성공")
                //.memberId(script.getMemberId().getId())
                .userId(member.getUserId())
                .username(member.getUsername())
                .phone_number(member.getPhoneNumber())
                .build();

        return ResponseEntity.ok(body);
    }


}
