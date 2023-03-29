package com.example.graduation4.member;

import com.example.graduation4.jwt.JwtTokenProvider;
import com.example.graduation4.member.dto.MemberRequestDto;
import com.example.graduation4.member.dto.MemberResponseDto;
import com.example.graduation4.resTemplate.ResponseException;
import com.example.graduation4.resTemplate.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.graduation4.resTemplate.ResponseTemplateStatus.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberResponseDto memberResponseDto;

    @Transactional
    public String login(MemberRequestDto.Login memberDto){
        Member member;
        try {
            member = memberRepository.findMemberByUserId(memberDto.getUserId());

            if (!passwordEncoder.matches(memberDto.getPassword(), member.getPassword())) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }
            // 로그인에 성공하면 email, roles 로 토큰 생성 후 반환
            return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());

        } catch (Exception e) {
            throw new IllegalArgumentException("없는 사용자입니다.");
        }

    }

    // 사용자 아이디 중복 check & 비밀번호 encrypt
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> createMember(MemberRequestDto.Register member1) throws ResponseException {
        System.out.println("service input user id: "+member1.getUserId());

        // 중복 확인: 해당 사용자 아이디를 가진 유저가 있는지 확인합니다. 중복될 경우, 에러 메시지를 보냅니다.
        if (checkUserId(member1.getUserId())== 1) {
            System.out.println("check user id - no user id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(USERID_DUPLICATED));
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("동일한 아이디를 사용하는 사용자가 있습니다. 아이디를 변경해주세요");
        }
        System.out.println("check user id - yes user id");
        String encrypted_password = passwordEncoder.encode(member1.getPassword());
        try {
            // encrypted_password = new AES128(SecurityConfig.USER_INFO_PASSWORD_KEY).encrypt(member1.getPassword()); // 암호화코드
            member1.setPassword(encrypted_password);

        } catch (Exception ignored) { // 암호화가 실패하였을 경우 에러 발생
            throw new ResponseException(INVALID_JWT);
        }

        Member new_member = memberRepository.createMember(member1);
        return memberResponseDto.memberSuccess(new_member);
    }


    // 해당 userId이 이미 Member Table에 존재하는지 확인
    public int checkUserId(String userId) throws ResponseException {
        try {
            return memberRepository.checkUserId(userId);
        } catch (Exception exception) {
            throw new ResponseException(DATABASE_ERROR);
        }
    }

    // 해당 userId이 이미 Member Table에 존재하는지 확인
    public int checkPassword(String password) throws ResponseException {
        try {
            return memberRepository.checkPassword(password);
        } catch (Exception exception) {
            throw new ResponseException(DATABASE_ERROR);
        }
    }


    /*
    @Transactional (readOnly = true) //데이터 변경을 허용하지 않음 -> 정합성을 위함
    public Member login(MemberRequestDto.Login user) {
        return memberRepository.login(user);

    }

     */

    // userId로 특정 사용자 찾기
    public ResponseEntity<?> findMemberByUserId(String userId){
        Member find_member;
        try {
            find_member=memberRepository.findMemberByUserId(userId);
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseTemplate<>(USER_NOT_FOUND));
        }
        return memberResponseDto.memberSuccess(find_member);
    }


    public Member returnMemberByUserId(String userId){

        Member find_member;
        try {
            find_member=memberRepository.findMemberByUserId(userId);
        } catch (EmptyResultDataAccessException e){
            find_member=new Member();
        }
        return find_member;
    }

}
