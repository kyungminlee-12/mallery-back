package com.example.graduation4.post;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

public class PostRequestDto {

    @Getter
    @Setter
    public static class Register {

        private Long albumId;

        private String postLocation;
        private String postDate;
        private List<String> participants;
    }

}
