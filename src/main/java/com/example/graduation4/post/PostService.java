package com.example.graduation4.post;

import com.example.graduation4.group.Album;
import com.example.graduation4.group.AlbumRepository;
import com.example.graduation4.group.AlbumService;

import com.example.graduation4.member.dto.Response;
import com.example.graduation4.post.dto.PostRequestDto;
import com.example.graduation4.post.dto.PostRes;
import com.example.graduation4.post.dto.PostResponseDto;
import com.example.graduation4.resTemplate.ResponseException;
import com.example.graduation4.resTemplate.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.graduation4.resTemplate.ResponseTemplateStatus.ALBUM_NOT_FOUND;
import static com.example.graduation4.resTemplate.ResponseTemplateStatus.POST_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class PostService {

    @Autowired
    private final AlbumRepository albumRepository;
    @Autowired
    private final PostRepository postRepository;
    private final Response response;
    private final PostResponseDto postResponseDto;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createPost(PostRequestDto.Register post1) throws ResponseException {

        if (albumRepository.checkAlbumId(post1.getAlbumId())!= 1) {
            System.out.println("album id: "+post1.getAlbumId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(ALBUM_NOT_FOUND));
        }

        Post new_album = postRepository.createPost(post1);
        return postResponseDto.postSuccess(new_album);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findPostByPostId(Long postId){
        Post find_post;
        try {
            find_post=postRepository.findPostById(postId);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(POST_NOT_FOUND));
        }
        return postResponseDto.postSuccess(find_post);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findPostsByGroupId(Long groupId) {
        List<PostRes> res_list = postRepository.getPosts(groupId);
        return postResponseDto.postList(groupId, res_list);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updatePost(Long postId, PostRequestDto.Update update_post ) {
        if (postRepository.checkPostId(postId)!=1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(POST_NOT_FOUND));
        }

        Post updated_post = postRepository.updatePost( postId, update_post);
        return postResponseDto.postSuccess(updated_post);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deletePost(Long postId) {
        if (postRepository.checkPostId(postId)!=1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(POST_NOT_FOUND));
        }
        postRepository.deletePost(postId);
        return postResponseDto.deletePost(postId);
    }




}
