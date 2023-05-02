package com.example.graduation4.post.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

public class PostRequestDto {

    @Getter
    @Setter
    public static class Register {

        private Long albumId;

        private String postLocation;
        private String postDate;
        private List<String> participants;
        private List<String> imagePaths;
    }
}
