package com.example.graduation4.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRes {

    private Long commentId;
    private String writer;
    private String content;
    private String date;
}
