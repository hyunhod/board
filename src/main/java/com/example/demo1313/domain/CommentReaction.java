package com.example.demo1313.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommentReaction {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    public enum ReactionType{
        LIKE, UNLIKE
    }
}
