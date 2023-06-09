package com.example.graduation4.post;


import com.example.graduation4.group.dto.AlbumRequestDto;
import com.example.graduation4.post.dto.PostRequestDto;
import com.example.graduation4.resTemplate.ResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/post")
@Api(tags = {"post API"})
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private final PostService postService;

    @PostMapping("/new")
    @ApiOperation(value = "post 생성")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> newPost(@RequestBody PostRequestDto.Register new_post ) throws ResponseException {
        return postService.createPost(new_post);
    }

    @GetMapping("/{groupId}/{postId}")
    @ApiOperation(value = "post 정보 받기")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> getPost(@PathVariable("postId") Long postId ) throws ResponseException {
        return postService.findPostByPostId(postId);
    }

    @GetMapping("/{groupId}/list")
    @ApiOperation(value = "post 정보 받기")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> getPosts(@PathVariable("groupId") Long groupId ) throws ResponseException {
        return postService.findPostsByGroupId(groupId);
    }

    @PutMapping("/{groupId}/{postId}")
    @ApiOperation(value = "post 정보 수정")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId, @RequestBody PostRequestDto.Update update_post ) throws ResponseException {
        return postService.updatePost(postId, update_post);
    }

    @DeleteMapping("/{groupId}/{postId}")
    @ApiOperation(value = "post 삭제")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> deletePosts(@PathVariable("postId") Long postId ) throws ResponseException {
        return postService.deletePost(postId);
    }

    @PostMapping("/{postId}/idCheck")
    @ApiOperation(value = "post 수정 가능 여부 확인")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> isUpdatable(@PathVariable("postId") Long postId, @RequestBody PostRequestDto.Check check_post ) throws ResponseException {
        return postService.isUpdatable(postId, check_post.getUserId());
    }
}
