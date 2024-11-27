package com.spring.emprendedoresApp.services;

import com.spring.emprendedoresApp.persistence.entities.CommentEntity;

import java.util.List;

public interface ICommentService {
    CommentEntity createComment(CommentEntity comment, Long postId);
    List<CommentEntity> getAllComments();
    List<CommentEntity> getCommentsByPost(Long postId);
    CommentEntity updateComment(Long id, CommentEntity updatedComment);
    boolean deleteComment(Long id);
}
