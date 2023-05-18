package com.example.graduation4.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRes {
    private Long albumId;
    private String albumName;
    private int memberCnt;
    private List<String> members;
    private List<String> nicknames;
}
