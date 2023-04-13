package com.example.graduation4.group;

import com.example.graduation4.group.dto.AlbumRequestDto;
import com.example.graduation4.resTemplate.ResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController      // Json 형태로 객체 데이터를 반환 (@Controller + @ResponseBody)
@RequestMapping("/album")
@Api(tags = {"앨범 API"})
@RequiredArgsConstructor
public class AlbumController {

    @Autowired
    private final AlbumService albumService;

    @PostMapping("/new")
    @ApiOperation(value = "앨범 생성")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> newAlbum(@RequestBody AlbumRequestDto.Register new_group ) throws ResponseException {
        return albumService.createAlbum(new_group);
    }

    @PutMapping("/member/add")
    @ApiOperation(value = "사용자 추가 생성")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> addMember(@RequestBody AlbumRequestDto.AddMember update_album ) throws ResponseException {
        return albumService.addMember(update_album);
    }

    @GetMapping("/members")
    @ApiOperation(value = "사용자 추가 생성")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> findFriends(@RequestBody AlbumRequestDto.FindFriends friends_list ) throws ResponseException {
        return albumService.findFriends(friends_list);
    }


}
