package com.example.graduation4.member;

import com.example.graduation4.config.SecurityUtil;
import com.example.graduation4.jwt.JwtTokenProvider;
import com.example.graduation4.member.dto.MemberRequestDto;
import com.example.graduation4.member.dto.MemberResponseDto;
import com.example.graduation4.member.dto.Response;
import com.example.graduation4.resTemplate.ResponseException;
import com.example.graduation4.resTemplate.ResponseTemplate;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

import static com.example.graduation4.resTemplate.ResponseTemplateStatus.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberResponseDto memberResponseDto;

    private final Response response;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;

    public ResponseEntity<?> login(MemberRequestDto.Login login) throws ResponseException {

        if (checkUserId(login.getUserId())!= 1) {
            return response.fail("해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(MemberRequestDto.Reissue reissue) {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(reissue.getAccessToken());

        // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
        // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
        if(ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if(!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 4. 새로운 토큰 생성
        MemberResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 5. RefreshToken Redis 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "Token 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(MemberRequestDto.Logout logout) {
        // 1. Access Token 검증
        if (!jwtTokenProvider.validateToken(logout.getAccessToken())) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }

        // 2. Access Token 에서 User email 을 가져옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(logout.getAccessToken());

        // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(logout.getAccessToken());
        redisTemplate.opsForValue()
                .set(logout.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    public ResponseEntity<?> authority() {
        // SecurityContext에 담겨 있는 authentication userEamil 정보
        String userId = SecurityUtil.getCurrentUserEmail();

        Member user = memberRepository.findMemberByUserId(userId);

        // add ROLE_ADMIN
        user.getRoles().add(Authority.ROLE_ADMIN.name());
        memberRepository.createMember(user);

        return response.success();
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
