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

import java.util.ArrayList;
import java.util.List;

@Component
public class PostResponseDto {

    @Autowired
    private PostRepository postRepository;


    @Getter
    @Builder
    private static class PostBody {
        private int state;
        private String result;
        private String postDate;
        private String postLocation;
        private String userId;
        private int memberCnt;
        private List<String> members;
        private List<String> nicknames;
        private List<String> imagePaths;
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
                .postDate(post1.getPostDate())
                .postLocation(post1.getPostLocation())
                .userId(members_list.get(0).getUserId())
                .memberCnt(members_list.size())
                .imagePaths(post1.getImagePaths())
                .members(members_li)
                .nicknames(nicknames_li)
                .build();

        return ResponseEntity.ok(body);
    }
}
