package com.example.graduation4.comment;

import com.example.graduation4.BaseEntity;
import com.example.graduation4.config.StringListConverter;
import com.example.graduation4.group.Album;
import com.example.graduation4.member.Member;
import com.example.graduation4.post.Participant;
import com.example.graduation4.post.Post;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id                                                  // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    @Column(name="comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post postId;

    // 자신의 위치 정보 입력
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member writer;

    @Column
    private String content;

    @Column
    private Boolean deleted;

}
