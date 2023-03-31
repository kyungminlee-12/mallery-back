package com.example.graduation4.member;

import com.example.graduation4.member.dto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @Autowired
    private final EntityManager em;
    @Autowired
    private final JdbcTemplate jdbcTemplate ;

    @Transactional(rollbackFor = Exception.class)
    public Member createMember(MemberRequestDto.Register member1) {
        System.out.println("repository input user id: "+member1.getUserId());

        Member member = new Member();
        member.setUserId(member1.getUserId());
        member.setUsername(member1.getUsername());
        member.setPassword(member1.getPassword());
        member.setPhoneNumber(member1.getPhoneNumber());
        member.setRoles(Collections.singletonList("ROLE_USER"));
        em.persist(member);

        return member;
    }

    @Transactional(rollbackFor = Exception.class)
    public Member createMember(Member member1) {
        em.persist(member1);
        return member1;
    }

    public int checkUserId(String userId) {
        try {
            // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
            String checkUserIdQuery = "select exists(select * from mallery.member where user_id = ?)";
            int res=this.jdbcTemplate.queryForObject(checkUserIdQuery, Integer.class, userId);
            System.out.println("check user id function repository: "+res);
            return res;
        } catch (Exception e){
            return 0;
        }
    }

    public int checkPassword(String password) {
        try {
            // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
            String checkPasswordQuery = "select exists(select * from mallery.member where password = ?)"; // User Table에 해당 email 값을 갖는 유저 정보가 존재하는가?
            return this.jdbcTemplate.queryForObject(checkPasswordQuery, Integer.class, password);
        } catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public Long findMemberIdByUserId(String userId) {

        String getUsersByUserIdQuery = "select member_id from mallery.member where user_id = ? ";
        // String getUsersByUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(getUsersByUserIdQuery, Long.class, userId);
    }

    @Transactional(readOnly = true)
    public Member findMemberByUserId(String userId) {

        String getUsersByUserIdQuery = "select member_id from mallery.member where user_id = ? ";
        Long member_id=jdbcTemplate.queryForObject(getUsersByUserIdQuery, Long.class, userId);
        return em.find(Member.class, member_id);
    }

    @Transactional(readOnly = true)
    public String findUsernameByUserId(String memberId) {
        String getUsernameQuery = "select username from mallery.member where user_id = ?";
        return this.jdbcTemplate.queryForObject(getUsernameQuery, String.class, memberId);
    }


}
