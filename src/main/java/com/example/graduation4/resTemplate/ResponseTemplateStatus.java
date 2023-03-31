package com.example.graduation4.resTemplate;

import lombok.Getter;

@Getter
public enum ResponseTemplateStatus {
    SUCCESS(true, "요청 성공", 1000),
    FAIL(false, "요청 실패", 1004),

    EMPTY_JWT(false, "JWT를 입력해주세요.", 2001),
    INVALID_JWT(false, "유효하지 않은 JWT입니다.", 2002),
    NO_JWT(false, "해당 JWT를 가진 사람이 없습니다.", 2003),

    DATABASE_ERROR(false, "데이버베이스 요청 오류", 3000),
    PATCH_DATABASE_ERROR(false, "닉네임 수정 데이터베이스 요청 오류", 3001),
    QUERY_ERROR(false,"쿼리 오류", 3002),
    //4000: 유저 부분 오류
    EMPTY_USERID(false, "사용자 아이디를 입력해주세요",4001),
    EMPTY_UPDATE(false, "변경할 사항이 없습니다.", 4002),
    EMPTY_PASSWORD(false, "비밀번호를 입력해주세요", 4003),
    EMPTY_USERNAME(false, "사용자 이름을 입력해주세요,", 4004),
    USER_NOT_FOUND(false, "유저를 찾을 수 없습니다.", 4005),
    FRIEND_NOT_FOUND(false, "친구를 찾을 수 없습니다.", 4005),

    USERID_DUPLICATED(false, "이미 존재하는 사용자 아이디입니다.", 4010),
    PASSWORD_DUPLICATED(false, "이미 존재하는 비밀번호입니다.", 4011),
    FRIEND_DUPLICATED(false, "이미 존재하는 친구 아이디입니다.", 4012),
    SIGNUP_SUCCESS(true, "해당 아이디 사용이 가능합니다", 200);

    private final boolean isSuccess;
    private final String message;
    private final int code;

    private ResponseTemplateStatus(boolean isSuccess, String message, int code){
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
