package com.example.graduation4.comment.dto;

import com.example.graduation4.comment.Comment;
import com.example.graduation4.member.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CommentResponseDto {

    @Getter
    @Builder
    private static class Body {
        private String result;
        private String writer;
        private String content;

        private String date;
    }

    @Getter
    @Builder
    private static class Comments {
        private String result;
        private int count;
        private List<CommentRes> comments;
    }

    @Getter
    @Builder
    private static class Delete {
        private String result;
        private Long commentId;
        private String message;
    }

    public ResponseEntity<?> commentSuccess(Comment comment, String nickname) {
        LocalDateTime modifiedDate = comment.getModifiedDate();
        String convertedDate1 = modifiedDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
        Body body = Body.builder()
                .result("success")
                .writer(nickname)
                .content(comment.getContent())
                .date(convertedDate1)
                .build();
        return ResponseEntity.ok(body);
    }

    // allComments
    public ResponseEntity<?> allComments(List<CommentRes> res_li) {

        Comments body = Comments.builder()
                .result("success")
                .count(res_li.size())
                .comments(res_li)
                .build();

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> commentDeleted(Long commentId) {

        Delete body = Delete.builder()
                .result("success")
                .commentId(commentId)
                .message("댓글 삭제 성공")
                .build();
        return ResponseEntity.ok(body);
    }

}
