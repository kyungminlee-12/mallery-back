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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
