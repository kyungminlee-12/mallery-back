package com.example.graduation4.group;

import com.example.graduation4.group.dto.AlbumRequestDto;
import com.example.graduation4.group.dto.AlbumRes;
import com.example.graduation4.member.Member;
import com.example.graduation4.member.MemberRepository;
import com.example.graduation4.member.dto.MemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AlbumRepository {

    @Autowired
    private final EntityManager em;

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
        room.setAlbum_user_name(init_member.getUsername());
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
            cur_member.setUsername(rooms.getAlbum_user_name());

            results.add(cur_member);
        }

        return results;
    }

    @Transactional(rollbackFor = Exception.class)
    public int addMember(AlbumRequestDto.AddMember album1) {
        // transaction.begin();

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
        room.setAlbum_user_name(init_member.getUsername());
        em.persist(room);

        cur_album.setMemberCnt(cur_album.getMemberCnt()+1);
        em.persist(cur_album);
        // transaction.commit();

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

    public int checkUserInAlbum(Long albumId, String userId) {
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

    @Transactional(rollbackFor = Exception.class)
    public Album update(Long albumId, AlbumRequestDto.Update update) {

        // transaction.begin();
        Album cur_album = em.find(Album.class, albumId);
        cur_album.setAlbumName(update.getAlbumName());
        em.persist(cur_album);
        // transaction.commit();

        return cur_album;
    }

    @Transactional(rollbackFor = Exception.class)
    public String deleteAlbumUser(Long albumId, AlbumRequestDto.Delete userId) {
        try {
            // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
            Member find_member=memberRepository.findMemberByUserId(userId.getUserId());
            Long member_id = find_member.getMemberId();
            System.out.println("delete album user member id: "+member_id);

            String findRoomQuery = "SELECT count(*) FROM mallery.rooms where album_id = ? and member_id = ?";
            int member_exits = this.jdbcTemplate.queryForObject(findRoomQuery, Integer.class , albumId, member_id);
            System.out.println("member exits: "+member_exits);

            // 해당 album에 user 없음
            if (member_exits != 1) {

                return "album에 아이디가 "+userId.getUserId()+"인 사용자가 없습니다.";
            }
            else {
                String sql = "delete FROM mallery.rooms where  album_id = "+albumId+" and member_id = "+member_id;
                int result=this.jdbcTemplate.update(sql);
                System.out.println(result+" row delete success");
            }

            String countMemberQuery = "select count(*) from mallery.rooms where album_id = ?";
            int count_member = this.jdbcTemplate.queryForObject(countMemberQuery, Integer.class, albumId);

            if (count_member==0) {
                Album to_delete_album = em.find(Album.class, albumId);
                em.remove(em.contains(to_delete_album) ? to_delete_album : em.merge(to_delete_album));
                // album에 남은 사용자가 없어서 사용자 모두 삭제
                return "album에서 "+userId.getUserId()+" 사용자가 성공적으로 삭제되었습니다 (album에 남은 사용자가 없어서 album도 삭제되었습니다.).";
            }
            // 사용자만 삭제. (아직 album에 사용자 남아 있음)
            return "album에서 "+userId.getUserId()+" 사용자가 성공적으로 삭제되었습니다.";


        } catch (Exception e){
            System.out.println("error : "+e);
            return "album에 아이디가 "+userId.getUserId()+"인 사용자가 없습니다.";
        }
        
    }

    // 사용자 친구들 정보 불러오기
    @Transactional(readOnly = true)
    public List<AlbumRes> getAlbums(String userId) {
        Member cur_member=memberRepository.findMemberByUserId(userId);

        List<Room> rooms_list = cur_member.getRooms();
        List<AlbumRes> results = new ArrayList<AlbumRes>();

        for (Room rooms : rooms_list) {
            AlbumRes cur_album = new AlbumRes();
            Long albumId = rooms.getAlbum().getAlbumId();
            cur_album.setAlbumId(albumId);
            cur_album.setAlbumName(rooms.getAlbum().getAlbumName());
            cur_album.setMemberCnt(rooms.getAlbum().getMemberCnt());

            List<String> members_list = new ArrayList<>();
            List<String> nicknames_list = new ArrayList<>();
            List<MemberRes> members_entity = findAllMembersByAlbumId(albumId);

            for (MemberRes member: members_entity)
                members_list.add(member.getUserId());
            cur_album.setMembers(members_list);

            cur_album.setNicknames(getAlbumNicknames(albumId));
            results.add(cur_album);
        }

        return results;
    }

    public List<String> getAlbumNicknames(Long albumId) {
        return jdbcTemplate.query("select album_user_name from rooms where album_id = "+albumId, new RowMapper<String>(){
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("album_user_name");
            }
        });
    }

    @Transactional(readOnly = true)
    public List<AlbumRes> getAlbums_before(String userId) {
        Member cur_member=memberRepository.findMemberByUserId(userId);

        List<Room> rooms_list = cur_member.getRooms();
        List<AlbumRes> results = new ArrayList<AlbumRes>();

        for (Room rooms : rooms_list) {
            AlbumRes cur_album = new AlbumRes();
            cur_album.setAlbumId(rooms.getAlbum().getAlbumId());
            cur_album.setAlbumName(rooms.getAlbum().getAlbumName());
            cur_album.setMemberCnt(rooms.getAlbum().getMemberCnt());

            List<String> members_list = new ArrayList<>();
            List<MemberRes> members_entity = findAllMembersByAlbumId(rooms.getAlbum().getAlbumId());

            for (MemberRes member: members_entity) {
                members_list.add(member.getUserId());
            }
            cur_album.setMembers(members_list);
            results.add(cur_album);
        }

        return results;
    }

    @Transactional(rollbackFor = Exception.class)
    public Album changeMemberName(AlbumRequestDto.UpdateUsername user_info) {

        Member cur_member = memberRepository.findMemberByUserId(user_info.getUserId());
        String findRoomQuery = "SELECT room_id FROM mallery.rooms where album_id = ? and member_id = ?";
        Long room_id = this.jdbcTemplate.queryForObject(findRoomQuery, Long.class , user_info.getAlbumId() , cur_member.getMemberId());
        Room changed_room = em.find(Room.class, room_id);
        System.out.println("changed room id: "+room_id);

        Album album = em.find(Album.class, user_info.getAlbumId());
        int room_idx = album.getRooms().indexOf(changed_room);
        System.out.println("changed room index of: "+room_idx);

        this.jdbcTemplate.update("update rooms set album_user_name = ? where album_id = ? and member_id = ? ", user_info.getUsername() , user_info.getAlbumId() , cur_member.getMemberId());

        if(room_idx != -1) {
            album.getRooms().get(room_idx).setAlbum_user_name(user_info.getUsername());
        }
        System.out.println("changed nickname: "+album.getRooms().get(room_idx).getAlbum_user_name());

        em.merge(album);
        return album;
    }


}
