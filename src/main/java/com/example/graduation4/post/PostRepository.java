package com.example.graduation4.post;

import com.example.graduation4.group.Album;
import com.example.graduation4.group.Room;
import com.example.graduation4.member.Member;

import com.example.graduation4.member.MemberRepository;
import com.example.graduation4.member.dto.MemberRes;
import com.example.graduation4.post.dto.PostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final EntityManager em;


    @Transactional(rollbackFor = Exception.class)
    public Post createPost(PostRequestDto.Register post1) {

        Album cur_album = em.find(Album.class, post1.getAlbumId());

        Post new_post = new Post();
        new_post.setPostDate(post1.getPostDate());
        new_post.setPostLocation(post1.getPostLocation());
        new_post.setAlbum(cur_album);

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
            cur_member.setUsername(participant.getMember().getUsername());

            results.add(cur_member);
        }

        return results;
    }

     */

}
