package com.example.graduation4.group;

import com.example.graduation4.BaseEntity;
import com.example.graduation4.post.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "album")
public class Album extends BaseEntity {

    @Id                                                  // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    @Column(name="album_id")
    private Long albumId;

    @Column
    private String albumName;

    @Column
    private int memberCnt;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL,  targetEntity = Room.class , fetch = FetchType.EAGER)
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private List<Post> posts = new ArrayList<>();

}
