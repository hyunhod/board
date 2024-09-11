package com.example.demo1313.service;

import com.example.demo1313.domain.Comment;
import com.example.demo1313.domain.CommentReaction;
import com.example.demo1313.domain.Member;
import com.example.demo1313.repository.CommentReactionRepository;
import com.example.demo1313.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReactionRepository commentReactionRepository;

    public void likeComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalStateException("Comment not found"));

        Optional<CommentReaction> existingReaction = commentReactionRepository.findByCommentAndMember(comment, member);
        if (existingReaction.isEmpty()) {
            CommentReaction reaction = new CommentReaction();
            reaction.setComment(comment);
            reaction.setMember(member);
            reaction.setReactionType(CommentReaction.ReactionType.LIKE);
            commentReactionRepository.save(reaction);
            if (comment.getLikes() == null) {
                comment.setLikes(0);
            }

            comment.setLikes(comment.getLikes() + 1);
            commentRepository.save(comment);
        }else{
            CommentReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == CommentReaction.ReactionType.LIKE) {
                // 이미 좋아요 상태라면, 좋아요 취소
                comment.setLikes(comment.getLikes() - 1);
                commentReactionRepository.delete(reaction);
            } else if (reaction.getReactionType() == CommentReaction.ReactionType.UNLIKE) {
                // 싫어요 상태에서 좋아요로 변경
                comment.setUnlikes(comment.getUnlikes() - 1);
                comment.setLikes(comment.getLikes() + 1);
                reaction.setReactionType(CommentReaction.ReactionType.LIKE);
                commentReactionRepository.save(reaction);
            }
        }
    }

    public void unlikeComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalStateException("Comment not found"));
        Optional<CommentReaction> existingReaction = commentReactionRepository.findByCommentAndMember(comment, member);
        if (existingReaction.isEmpty()) {
            CommentReaction reaction = new CommentReaction();
            reaction.setComment(comment);
            reaction.setMember(member);
            reaction.setReactionType(CommentReaction.ReactionType.UNLIKE);
            commentReactionRepository.save(reaction);
            if (comment.getUnlikes() == null) {
                comment.setUnlikes(0);
            }

            comment.setUnlikes(comment.getUnlikes() + 1);
            commentRepository.save(comment);
        }else{
            CommentReaction reaction = existingReaction.get();
            if (reaction.getReactionType() == CommentReaction.ReactionType.UNLIKE) {
                // 이미 싫어요 상태라면, 싫어요 취소
                comment.setUnlikes(comment.getUnlikes() - 1);
                commentReactionRepository.delete(reaction);
            } else if (reaction.getReactionType() == CommentReaction.ReactionType.LIKE) {
                // 좋아요 상태에서 싫어요로 변경
                comment.setLikes(comment.getLikes() - 1);
                comment.setUnlikes(comment.getUnlikes() + 1);
                reaction.setReactionType(CommentReaction.ReactionType.UNLIKE);
                commentReactionRepository.save(reaction);
            }
        }
    }

        // 부모 댓글만 필터링하여 반환하는 메서드
        public List<Comment> getParentCommentsByBoardId (Long boardId){
            List<Comment> allComments = commentRepository.findByBoardId(boardId);
            List<Comment> parentComments = new ArrayList<>();

            for (Comment comment : allComments) {
                if (comment.getParent() == null) {
                    parentComments.add(comment); //부모댓글만 필터링
                }
            }
            return parentComments;
        }

    }
