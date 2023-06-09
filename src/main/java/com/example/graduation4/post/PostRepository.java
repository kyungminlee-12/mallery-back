package com.example.graduation4.post;

import com.example.graduation4.group.Album;
import com.example.graduation4.group.Room;
import com.example.graduation4.member.Member;

import com.example.graduation4.member.MemberRepository;
import com.example.graduation4.member.dto.MemberRes;
import com.example.graduation4.post.dto.PostRequestDto;
import com.example.graduation4.post.dto.PostRes;
import com.example.graduation4.post.dto.PostResponseDto;
import com.example.graduation4.resTemplate.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.example.graduation4.resTemplate.ResponseTemplateStatus.ALBUM_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final JdbcTemplate jdbcTemplate ;

    @Autowired
    private final EntityManager em;


    @Transactional(rollbackFor = Exception.class)
    public Post createPost(PostRequestDto.Register post1) {

        Album cur_album = em.find(Album.class, post1.getAlbumId());

        Post new_post = new Post();
        new_post.setPostDate(post1.getPostDate());
        new_post.setUserId(post1.getUserId());
        new_post.setPostLocation(post1.getPostLocation());
        new_post.setAlbum(cur_album);
        new_post.setImagePaths(post1.getImagePaths());
        new_post.setDeleted(false);
        // new_post.set(false);

        List<String> participants_ids = post1.getParticipants();
        List<Participant> participants_list = new ArrayList<>();

        for (int cnt=0; cnt < participants_ids.size(); cnt++) {
            System.out.println("cur participant: "+participants_ids.get(cnt));
            Member new_member = memberRepository.findMemberByUserId(participants_ids.get(cnt));

            Participant cur_participant = new Participant();
            cur_participant.setPost(new_post);
            cur_participant.setMember(new_member);
            participants_list.add(cur_participant);
            em.persist(cur_participant);
        }
        new_post.setParticipants(participants_list);

        em.persist(new_post);
        cur_album.getPosts().add(new_post);

        return new_post;
    }

    @Transactional(readOnly = true)
    public List<MemberRes> findAllMembersByPostId(Long postId) {

        Post cur_post = em.find(Post.class, postId);

        List<Participant> participants_list = cur_post.getParticipants();
        List<MemberRes> results = new ArrayList<MemberRes>();

        for (Participant participant : participants_list) {
            MemberRes cur_member = new MemberRes();
            cur_member.setUserId(participant.getMember().getUserId());

            String findRoomQuery = "SELECT room_id FROM mallery.rooms where album_id = ? and member_id = ?";
            System.out.println("album id: "+cur_post.getAlbum().getAlbumId());
            System.out.println("member id: "+ participant.getMember().getMemberId());
            Long room_id = this.jdbcTemplate.queryForObject(findRoomQuery, Long.class , cur_post.getAlbum().getAlbumId() , participant.getMember().getMemberId());
            System.out.println("room id: "+room_id);
            Room cur_room = em.find(Room.class, room_id);

            cur_member.setUsername(cur_room.getAlbum_user_name());
            // cur_member.setUsername(participant.getMember().getUsername());
            results.add(cur_member);
        }

        return results;
    }

    public Post findPostById(Long postId) {
        return em.find(Post.class, postId);
    }

    public List<PostRes> getPosts(Long albumId) {
        Album cur_album = em.find(Album.class, albumId);
        System.out.println("album id: "+albumId+", album name: "+cur_album.getAlbumName());
        List<PostRes> res_li = new ArrayList<>();

        List<Post> post_li = cur_album.getPosts();
        System.out.println("post_li size: "+post_li.size());
        for (int idx=0 ; idx<post_li.size(); idx++) {
            Post cur_post = post_li.get(idx);
            // if (cur_post.getDeleted() == true) continue;
            PostRes new_res = new PostRes();
            new_res.setPostId(cur_post.getPostId());
            new_res.setPostDate(cur_post.getPostDate());
            new_res.setMainImage(cur_post.getImagePaths().get(0));
            res_li.add(new_res);
        }
        return res_li;
    }

    public int checkPostId(Long postId) {
        try {
            // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
            String checkUserIdQuery = "select exists(select * from mallery.post where post_id = ?)";
            return this.jdbcTemplate.queryForObject(checkUserIdQuery, Integer.class, postId);

        } catch (Exception e){
            return 0;
        }
    }

    public void deletePost(Long postId) {
        Post cur_post = em.find(Post.class, postId);
        cur_post.setDeleted(true);
        em.persist(cur_post);
    }

    public Post updatePost(Long postId, PostRequestDto.Update update_post ) {
        Post cur_post = em.find(Post.class, postId);
        cur_post.setPostLocation(update_post.getPostLocation());
        cur_post.setPostDate(update_post.getPostDate());
        cur_post.setUserId(update_post.getUserId());

        /*
        List<String> participants_ids = update_post.getParticipants();
        List<Participant> participants_list = new ArrayList<>();

        for (int cnt=0; cnt < participants_ids.size(); cnt++) {
            System.out.println("cur participant: "+participants_ids.get(cnt));
            Member new_member = memberRepository.findMemberByUserId(participants_ids.get(cnt));

            Participant cur_participant = new Participant();
            cur_participant.setPost(cur_post);
            cur_participant.setMember(new_member);
            participants_list.add(cur_participant);
            em.persist(cur_participant);
        }

        cur_post.setParticipants(participants_list);

         */
        cur_post.setImagePaths(update_post.getImagePaths());
        cur_post.setUpdated(true);
        em.persist(cur_post);

        return cur_post;
    }


    public int isUpdatable (Long postId, String userId) {
        Member cur_mem = memberRepository.findMemberByUserId(userId);
        String checkQuery = "select exists(select * from mallery.participant where post_id = ? and member_id = ?)";
        return this.jdbcTemplate.queryForObject(checkQuery, Integer.class, postId, cur_mem.getMemberId());

    }


}
