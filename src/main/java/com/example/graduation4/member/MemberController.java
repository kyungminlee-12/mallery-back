package com.example.graduation4.member;

import com.example.graduation4.member.dto.MemberRequestDto;
import com.example.graduation4.resTemplate.ResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Join;

@Slf4j
@RestController      // Json 형태로 객체 데이터를 반환 (@Controller + @ResponseBody)
@RequestMapping("/member")
@Api(tags = {"회원 API"})
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;


    // 로그인 API
    @PostMapping("/login")
    public String login(@RequestBody MemberRequestDto.Login memberDto) {
        return memberService.login(memberDto);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> signUp(@RequestBody MemberRequestDto.Register register_input ) throws ResponseException {
        return memberService.createMember(register_input);
    }

    @GetMapping("/{userid}")
    public ResponseEntity<?>  findUserInfo( @PathVariable("userid") String userid ) {
        return memberService.findMemberByUserId(userid);
    }

}
