package com.example.graduation4.group;

import com.example.graduation4.group.dto.AlbumRequestDto;
import com.example.graduation4.member.Member;
import com.example.graduation4.member.MemberRepository;
import com.example.graduation4.member.dto.MemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepository {

    @Autowired
     EntityManager em;
    @Autowired
    private final JdbcTemplate jdbcTemplate ;
    @Autowired
    private final MemberRepository memberRepository;

    @Transactional(rollbackFor = Exception.class)
    public Album createAlbum(AlbumRequestDto.Register album1) {
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

    @Transactional(readOnly = true)
    public List<MemberRes> findAllMembersByAlbumId(Long albumId) {

        Album cur_album = em.find(Album.class, albumId);

        List<Room> rooms_list = cur_album.getRooms();
        List<MemberRes> results = new ArrayList<MemberRes>();

        for (Room rooms : rooms_list) {
            MemberRes cur_member = new MemberRes();
            cur_member.setUserId(rooms.getMember().getUserId());
            cur_member.setUsername(rooms.getMember().getUsername());

            results.add(cur_member);
        }

        return results;
    }

    @Transactional(rollbackFor = Exception.class)
    public int addMember(AlbumRequestDto.AddMember album1) {

        Member init_member = memberRepository.findMemberByUserId(album1.getUserId());
        Album cur_album=em.find(Album.class, album1.getAlbumId());

        Long cur_id = init_member.getMemberId();
        List<Room> rooms_list = cur_album.getRooms();

        for (Room rooms : rooms_list) {
            if (rooms.getMember().getMemberId() == cur_id)
                return 2;
        }

        Room room=new Room();
        room.addMember(init_member);
        room.addGroup(cur_album);
        em.persist(room);

        cur_album.setMemberCnt(cur_album.getMemberCnt()+1);
        em.persist(cur_album);

        return 0;
    }

    public int checkAlbumId(Long albumId) {
        try {
            // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
            String checkQuery = "select exists(select * from mallery.album where album_id = ?)";
            return this.jdbcTemplate.queryForObject(checkQuery, Integer.class, albumId);
        } catch (Exception e){
            return 0;
        }
    }

    public Album findAlbumById(Long albumId) {
        return em.find(Album.class, albumId);
    }

}
