package com.example.graduation4.group;

import com.example.graduation4.member.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @Column
    private String album_user_name;

    public void addMember(Member member){
        if(member.getRooms().contains(this)){
            member.getRooms().remove(this);
        }

        this.member = member;
        member.getRooms().add(this);
    }

    public void addGroup(Album album){
        this.album = album;
        album.getRooms().add(this);
    }

}
