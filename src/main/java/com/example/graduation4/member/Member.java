package com.example.graduation4.member;


import com.example.graduation4.BaseEntity;
import com.example.graduation4.group.Room;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "member")
public class Member extends BaseEntity implements UserDetails {

    @Id                                                  // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    @Column(name="member_id")
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column
    private String username;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String password;

    @Column
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER) //roles 컬렉션
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override   //사용자의 권한 목록 리턴
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @OneToMany(mappedBy = "member")
    private List<Room> rooms = new ArrayList<>();

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
