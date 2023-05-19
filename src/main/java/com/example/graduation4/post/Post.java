package com.example.graduation4.post;

import com.example.graduation4.BaseEntity;
import com.example.graduation4.config.StringListConverter;
import com.example.graduation4.group.Album;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED) // 데이터 변경 이력 감지
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "post")
public class Post extends BaseEntity  {

    @Id                                                  // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    @Column(name="post_id")
    private Long postId;

    // @Column
    // private String postLocation;

    // 자신의 위치 정보 입력
    @Column
    private String postLocation;

    @Column
    private String postDate;

    @Column
    private String userId;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @NotAudited
    private Album album;

    @Column
    private boolean isUpdated;

    @Builder.Default
    @OneToMany(mappedBy = "post")
    @NotAudited
    private List<Participant> participants = new ArrayList<>();

    // 이미지 경로
    @Builder.Default
    @Convert(converter = StringListConverter.class)
    @Column(name="paths")
    private List<String> imagePaths = new ArrayList<>();

}
