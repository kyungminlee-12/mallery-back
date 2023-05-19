package com.example.graduation4.comment;

import com.example.graduation4.comment.dto.CommentRequestDto;
import com.example.graduation4.comment.dto.CommentRes;
import com.example.graduation4.group.Room;
import com.example.graduation4.member.Member;
import com.example.graduation4.member.MemberRepository;
import com.example.graduation4.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final JdbcTemplate jdbcTemplate ;
    @Autowired
    private final EntityManager em;

    @Transactional(rollbackFor = Exception.class)
    public Comment createComment(CommentRequestDto.Writer comment1) {
        Member cur_mem = memberRepository.findMemberByUserId(comment1.getUserId());
        Post cur_post = em.find(Post.class, comment1.getPostId() );

        Comment new_comment = new Comment();
        new_comment.setPostId(cur_post);
        new_comment.setWriter(cur_mem);
        new_comment.setContent(comment1.getContent());
        new_comment.setDeleted(false);
        em.persist(new_comment);

        return new_comment;
    }

    public String findNickName(Member cur_mem, Post cur_post) {
        String findRoomQuery = "SELECT room_id FROM mallery.rooms where album_id = ? and member_id = ?";
        Long room_id = this.jdbcTemplate.queryForObject(findRoomQuery, Long.class , cur_post.getAlbum().getAlbumId() , cur_mem.getMemberId());
        System.out.println("room id: "+room_id);
        Room cur_room = em.find(Room.class, room_id);

        return cur_room.getAlbum_user_name();
    }

    public List<CommentRes> getComments(Long postId) {
        String findRoomQuery = "SELECT comment_id, member_id, content, modified_date FROM mallery.comment where post_id = "+postId+" and deleted = false";
        Post cur_post = em.find(Post.class, postId);
        List<CommentRes> result = this.jdbcTemplate.query(findRoomQuery, new RowMapper<CommentRes>() {
                public CommentRes mapRow(ResultSet rs, int rowNum) throws SQLException {

                    Member member = em.find(Member.class, rs.getLong("member_id"));
                    String nickname = findNickName(member, cur_post);

                    LocalDateTime cur_date = rs.getObject("modified_date", LocalDateTime.class );
                    String date = cur_date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

                    CommentRes comment = new CommentRes();
                    comment.setCommentId(rs.getLong("comment_id"));
                    comment.setWriter(nickname);
                    comment.setContent(rs.getString("content"));
                    comment.setDate(date);
                    return comment;
                }
            }
        );
        return result;
    }

    public int checkCommentId(Long commentId) {
        try {
            // 쿼리문의 결과(존재하지 않음(False,0),존재함(True, 1))를 int형(0,1)으로 반환됩니다.
            String checkIdQuery = "select exists(select * from mallery.comment where comment_id = ?)";
            return this.jdbcTemplate.queryForObject(checkIdQuery, Integer.class, commentId);

        } catch (Exception e){
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        Comment cur_comment = em.find(Comment.class, commentId);
        cur_comment.setDeleted(true);
        em.persist(cur_comment);
    }

    @Transactional(rollbackFor = Exception.class)
    public Comment updateComment(Long commentId, CommentRequestDto.Update comment_info) {

        Comment new_comment = em.find(Comment.class, commentId);
        new_comment.setContent(comment_info.getContent());
        em.persist(new_comment);

        return new_comment;
    }

}
