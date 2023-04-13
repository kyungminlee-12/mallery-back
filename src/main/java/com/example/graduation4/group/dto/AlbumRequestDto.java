package com.example.graduation4.group.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AlbumRequestDto {

    @Getter
    @Setter
    public static class Register {

        @Pattern(regexp="[가-힣|a-z|A-Z|0-9]{1,30}",message="1~30길이의 알파벳, 한글, 숫자로 입력해주세요")
        @NotBlank(message="그룹 이름을 입력해주세요.")
        @NotNull
        private String albumName;
        private String userId;
    }

    @Getter
    @Setter
    public static class AddMember {
        private Long albumId;
        private String userId;
    }

    @Getter
    @Setter
    public static class FindFriends {
        private Long albumId;
    }

}
