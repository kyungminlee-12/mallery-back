package com.example.graduation4.group;

import com.example.graduation4.group.dto.AlbumRequestDto;
import com.example.graduation4.group.dto.AlbumResponseDto;
import com.example.graduation4.member.Member;
import com.example.graduation4.member.MemberService;
import com.example.graduation4.member.dto.Response;
import com.example.graduation4.resTemplate.ResponseException;
import com.example.graduation4.resTemplate.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.graduation4.resTemplate.ResponseTemplateStatus.*;


@RequiredArgsConstructor
@Service
public class AlbumService {

    @Autowired
    private final AlbumRepository albumRepository;
    @Autowired
    private final MemberService memberService;
    private final Response response;

    private final AlbumResponseDto albumResponseDto;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createAlbum(AlbumRequestDto.Register album1) throws ResponseException {
        System.out.println("service input album name: "+album1.getAlbumName());

        if (memberService.checkUserId(album1.getUserId())!= 1) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Album new_album = albumRepository.createAlbum(album1);
        return albumResponseDto.albumSuccess(new_album);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> addMember(AlbumRequestDto.AddMember album1) throws ResponseException {

        if (memberService.checkUserId(album1.getUserId())!= 1) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        if (albumRepository.checkAlbumId(album1.getAlbumId())!= 1) {
            return response.fail("해당하는 앨범이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        int album_status = albumRepository.addMember(album1);

        if (album_status == 2) {
            return response.fail("이미 추가된 사용자입니다.", HttpStatus.BAD_REQUEST);
        }
        return albumResponseDto.albumSuccess(albumRepository.findAlbumById(album1.getAlbumId()));
    }


    // findAllMembersByAlbumId
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> findFriends(AlbumRequestDto.FindFriends albumId) throws ResponseException {

        if (albumRepository.checkAlbumId(albumId.getAlbumId())!= 1) {
            return response.fail("해당하는 앨범이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        return albumResponseDto.friendsSuccess(albumRepository.findAlbumById(albumId.getAlbumId()));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findAlbumsByAlbumId(Long albumId){
        Album find_album;
        try {
            find_album=albumRepository.findAlbumById(albumId);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(ALBUM_NOT_FOUND));
        }
        return albumResponseDto.friendsSuccess(find_album);
    }

    public int checkAlbumId(Long albumId) throws ResponseException {
        try {
            return albumRepository.checkAlbumId(albumId);
        } catch (Exception exception) {
            throw new ResponseException(DATABASE_ERROR);
        }
    }

    //update
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> updateAlbum(Long albumId, AlbumRequestDto.Update update) throws ResponseException {

        // 중복 확인: 해당 사용자 아이디를 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
        if (checkAlbumId(albumId)!= 1) {
            return response.fail("해당하는 앨범이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Album updated_album = albumRepository.update(albumId, update);
        // Member new_member = memberRepository.createMember(member1);
        return albumResponseDto.albumSuccess(updated_album);
    }

    //update
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> deleteAlbumUser(Long albumId, AlbumRequestDto.Delete userId) throws ResponseException {

        // 중복 확인: 해당 사용자 아이디를 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
        if (checkAlbumId(albumId)!= 1) {
            return response.fail("해당하는 앨범이 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        if (memberService.checkUserId(userId.getUserId())!= 1) {
            return response.fail("해당하는 사용자가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String updated_album = albumRepository.deleteAlbumUser(albumId, userId);
        // Member new_member = memberRepository.createMember(member1);
        return albumResponseDto.albumDelete(updated_album, albumId);
    }

}
