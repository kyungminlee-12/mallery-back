package com.example.graduation4.post;

import com.example.graduation4.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

// Member와 Post 다:다 양방향 mapping
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /*
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
     */

}
