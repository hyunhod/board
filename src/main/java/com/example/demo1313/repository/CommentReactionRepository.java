package com.example.demo1313.repository;

import com.example.demo1313.domain.Comment;
import com.example.demo1313.domain.CommentReaction;
import com.example.demo1313.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction,Long> {
    Optional<CommentReaction> findByCommentAndMember(Comment comment, Member member);
}
