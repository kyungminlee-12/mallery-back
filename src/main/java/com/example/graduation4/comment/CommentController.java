package com.example.graduation4.comment;

import com.example.graduation4.comment.dto.CommentRequestDto;
import com.example.graduation4.resTemplate.ResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comment")
@Api(tags = {"comment API"})
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/new")
    @ApiOperation(value = "comment 생성")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> newComment(@RequestBody CommentRequestDto.Writer new_comment ) throws ResponseException {
        return commentService.createComment(new_comment);
    }

    @GetMapping("/{postId}/list")
    @ApiOperation(value = "comment 정보 받기")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> getComments(@PathVariable("postId") Long postId ) throws ResponseException {
        return commentService.findCommentsByPostId(postId);
    }

    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "comment 삭제")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) throws ResponseException {
        return commentService.deleteComment(commentId);
    }

    @PutMapping("/{commentId}")
    @ApiOperation(value = "comment 정보 수정")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> getPosts(@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto.Update comment_info) throws ResponseException {
        return commentService.updateComment(commentId, comment_info);
    }


}
