package com.example.demo1313.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="comment1")
public class Comment {
    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;

    //대댓글기능
    @NotBlank
    private String content;
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name="parent_id")
    private Comment parent; //대댓글의 부모댓글

    @OneToMany(mappedBy = "parent")
    private List<Comment> replies = new ArrayList<>();//이댓글에 달린 대댓글 목록


    private Integer likes = 0;
    private Integer unlikes = 0;

    @OneToMany(mappedBy = "comment")
    private List<CommentReaction> reactions= new ArrayList<>();


}
