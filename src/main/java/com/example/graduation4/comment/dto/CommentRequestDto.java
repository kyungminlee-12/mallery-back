package com.example.graduation4.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class CommentRequestDto {

    @Getter
    @Setter
    public static class Writer {
        private Long postId;
        private String userId;
        private String content;
    }

    @Getter
    @Setter
    public static class Update {
        private String content;
    }

}
