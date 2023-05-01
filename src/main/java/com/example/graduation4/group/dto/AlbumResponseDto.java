package com.example.graduation4.group.dto;

import com.example.graduation4.group.Album;
import com.example.graduation4.group.AlbumRepository;
import com.example.graduation4.member.MemberRepository;
import com.example.graduation4.member.dto.MemberRes;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AlbumResponseDto {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AlbumRepository albumRepository;

    @Getter
    @Builder
    private static class AlbumBody {
        private int state;
        private String result;
        private Long albumId;
        private String albumName;
        private int memberCnt;
        private List<MemberRes> members;
    }

    @Getter
    @Builder
    private static class AlbumDelete {
        private int state;
        private String result;
        private Long albumId;
    }

    public ResponseEntity<?> albumSuccess(Album album1) {

        List<MemberRes> members_list = albumRepository.findAllMembersByAlbumId(album1.getAlbumId());

        AlbumBody body = AlbumBody.builder()
                .state(200)
                .result("성공: 앨범 정보")
                .albumId(album1.getAlbumId())
                .albumName(album1.getAlbumName())
                .memberCnt(album1.getMemberCnt())
                .members(members_list)
                .build();

        return ResponseEntity.ok(body);
    }

    // friendsSuccess
    public ResponseEntity<?> friendsSuccess(Album album1) {

        List<MemberRes> members_list = albumRepository.findAllMembersByAlbumId(album1.getAlbumId());

        AlbumBody body = AlbumBody.builder()
                .state(200)
                .result("앨범 정보 호출 성공")
                .albumId(album1.getAlbumId())
                .albumName(album1.getAlbumName())
                .memberCnt(album1.getMemberCnt())
                .members(members_list)
                .build();

        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> albumDelete(String message, Long albumId) {

        AlbumDelete body = AlbumDelete.builder()
                .state(200)
                .result(message)
                .albumId(albumId)
                .build();

        return ResponseEntity.ok(body);
    }



}
