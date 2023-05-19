package com.example.graduation4.comment;

import com.example.graduation4.comment.dto.CommentRequestDto;
import com.example.graduation4.comment.dto.CommentRes;
import com.example.graduation4.comment.dto.CommentResponseDto;
import com.example.graduation4.member.MemberService;
import com.example.graduation4.member.dto.Response;
import com.example.graduation4.post.Post;
import com.example.graduation4.post.PostRepository;
import com.example.graduation4.resTemplate.ResponseException;

import com.example.graduation4.resTemplate.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.nullability.AlwaysNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.graduation4.resTemplate.ResponseTemplateStatus.*;

@RequiredArgsConstructor
@Service
public class CommentService {

    @Autowired
    private final MemberService memberService;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final CommentRepository commentRepository;
    private final CommentResponseDto commentResponseDto;
    private final Response response;

    public ResponseEntity<?> createComment(CommentRequestDto.Writer comment1 ) throws ResponseException {

        if (memberService.checkUserId(comment1.getUserId())!= 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(USER_NOT_FOUND));
        }

        Comment new_comment = commentRepository.createComment(comment1);
        String nickname = commentRepository.findNickName(new_comment.getWriter(), new_comment.getPostId());
        return commentResponseDto.commentSuccess(new_comment, nickname);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findCommentsByPostId(Long postId) throws ResponseException {
        if (postRepository.checkPostId(postId)!=1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(POST_NOT_FOUND));
        }
        List<CommentRes> res_list = commentRepository.getComments(postId);
        return commentResponseDto.allComments(res_list);
    }

    public ResponseEntity<?> deleteComment(Long commentId) throws ResponseException {

        if (commentRepository.checkCommentId(commentId)!= 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(COMMENT_NOT_FOUNT));
        }
        commentRepository.deleteComment(commentId);
        return commentResponseDto.commentDeleted(commentId);
    }

    public ResponseEntity<?> updateComment(Long commentId, CommentRequestDto.Update comment_info) throws ResponseException {

        if (commentRepository.checkCommentId(commentId)!= 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(COMMENT_NOT_FOUNT));
        }
        Comment new_comment = commentRepository.updateComment(commentId, comment_info);
        String nickname = commentRepository.findNickName(new_comment.getWriter(), new_comment.getPostId());
        return commentResponseDto.commentSuccess(new_comment, nickname);
    }


}
