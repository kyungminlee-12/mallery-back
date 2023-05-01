package com.example.graduation4.post;

import com.example.graduation4.group.Album;
import com.example.graduation4.group.Room;
import com.example.graduation4.group.dto.AlbumRequestDto;
import com.example.graduation4.member.Member;
import com.example.graduation4.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    @Autowired
    private final MemberRepository memberRepository;
    /*

    @Transactional(rollbackFor = Exception.class)
    public Album createPost(AlbumRequestDto.Register album1) {
        System.out.println("album repository input album name: "+album1.getAlbumName());

        Member init_member = memberRepository.findMemberByUserId(album1.getUserId());
        System.out.println("album repository input user id: "+init_member.getUserId());

        Album album = new Album();
        album.setAlbumName(album1.getAlbumName());
        album.setMemberCnt(1);
        // group.setRooms(init_members);
        em.persist(album);
        System.out.println("album repository album: "+album.getAlbumName());

        Room room=new Room();
        room.addMember(init_member);
        room.addGroup(album);
        em.persist(room);
        System.out.println("album repository room username: "+room.getMember().getUsername());

        return album;
    }

     */

}
