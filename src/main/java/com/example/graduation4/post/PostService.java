package com.example.graduation4.post;

import com.example.graduation4.group.Album;
import com.example.graduation4.group.AlbumRepository;
import com.example.graduation4.group.AlbumService;

import com.example.graduation4.member.dto.Response;
import com.example.graduation4.post.dto.PostRequestDto;
import com.example.graduation4.post.dto.PostResponseDto;
import com.example.graduation4.resTemplate.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            return response.fail("해당하는 앨범이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Post new_album = postRepository.createPost(post1);
        return postResponseDto.postSuccess(new_album);
    }
}
