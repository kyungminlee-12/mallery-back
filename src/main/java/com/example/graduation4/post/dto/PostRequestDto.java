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
        private String userId;
        private List<String> participants;
        private List<String> imagePaths;
    }

    @Getter
    @Setter
    public static class Update {

        private String postLocation;
        private String postDate;
        private String userId;
        // private List<String> participants;
        private List<String> imagePaths;
    }

    @Getter
    @Setter
    public static class Check {
        private String userId;
    }
}
