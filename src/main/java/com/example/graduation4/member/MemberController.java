package com.example.graduation4.member;

import com.example.graduation4.jwt.JwtTokenProvider;
import com.example.graduation4.member.dto.MemberRequestDto;
import com.example.graduation4.member.dto.Response;
import com.example.graduation4.resTemplate.Helper;

import com.example.graduation4.resTemplate.ResponseException;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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
    private final Response response;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    // @ApiOperation(value = "회원가입", response = Join.class)
    public ResponseEntity<?> signUp(@RequestBody MemberRequestDto.Register register_input ) throws ResponseException {
        return memberService.createMember(register_input);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberRequestDto.Login login, Errors errors) throws ResponseException {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return memberService.login(login);
    }


    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody MemberRequestDto.Reissue reissue, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return memberService.reissue(reissue);
    }

    // @Validated
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody MemberRequestDto.Logout logout, Errors errors) {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return memberService.logout(logout);
    }

    @GetMapping("/authority")
    public ResponseEntity<?> authority(@RequestBody MemberRequestDto.Login login) throws ResponseException {
        log.info("ADD ROLE_ADMIN");
        return memberService.authority(login.getUserId());
    }

    @GetMapping("/{userid}")
    public ResponseEntity<?>  findUserInfo( @PathVariable("userid") String userid ) {
        return memberService.findMemberByUserId(userid);
    }

    @PostMapping("/idCheck")
    public ResponseEntity<?> doubleCheck(@RequestBody MemberRequestDto.IdCheck id, Errors errors) throws ResponseException {
        // validation check
        if (errors.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(errors));
        }
        return memberService.idCheck(id);
    }

}
