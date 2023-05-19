package com.example.graduation4.post.dto;

import com.example.graduation4.group.Album;
import com.example.graduation4.group.AlbumRepository;
import com.example.graduation4.group.dto.AlbumResponseDto;
import com.example.graduation4.member.MemberRepository;
import com.example.graduation4.member.dto.MemberRes;
import com.example.graduation4.post.Post;
import com.example.graduation4.post.PostRepository;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
public class PostResponseDto {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EntityManager em;


    @Getter
    @Builder
    private static class PostBody {
        private int state;
        private String result;
        private Long postId;
        private String postDate;
        private String postLocation;
        private String userId;
        private int memberCnt;
        private boolean updated;
        private List<String> members;
        private List<String> nicknames;
        private List<String> imagePaths;
    }

    @Getter
    @Builder
    private static class PostListBody {
        private int state;
        private String result;
        private Long albumId;
        private String albumName;
        private int memberCnt;
        // private String userId;
        private List<PostRes> posts;
    }

    @Getter
    @Builder
    private static class Delete {
        private String result;
        private Long commentId;
        private String message;
    }

    public ResponseEntity<?> postSuccess(Post post1) {

        List<MemberRes> members_list = postRepository.findAllMembersByPostId(post1.getPostId());
        List<String> members_li = new ArrayList<>();
        List<String> nicknames_li = new ArrayList<>();

        for (int i=0 ; i < members_list.size() ; i++) {
            members_li.add(members_list.get(i).getUserId());
            nicknames_li.add(members_list.get(i).getUsername());
        }
        PostBody body = PostBody.builder()
                .state(200)
                .result("성공: Post 정보")
                .postId(post1.getPostId())
                .postDate(post1.getPostDate())
                .postLocation(post1.getPostLocation())
                .userId(members_list.get(0).getUserId())
                .memberCnt(members_list.size())
                .updated(post1.isUpdated())
                .imagePaths(post1.getImagePaths())
                .members(members_li)
                .nicknames(nicknames_li)
                .build();

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> postList(Long albumId, List<PostRes> postli) {

        Album cur_album = em.find(Album.class, albumId);
        PostListBody body = PostListBody.builder()
                .state(200)
                .result("성공: Post 정보")
                .albumId(albumId)
                .albumName(cur_album.getAlbumName())
                .memberCnt(cur_album.getMemberCnt())
                .posts(postli)
                .build();

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> deletePost(Long postId) {

        Delete body = Delete.builder()
                .result("success")
                .commentId(postId)
                .message("게시물 삭제 성공")
                .build();
        return ResponseEntity.ok(body);
    }


}
