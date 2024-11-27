package com.spring.emprendedoresApp.services;

import com.spring.emprendedoresApp.persistence.entities.PostEntity;

import java.util.List;

public interface IPostService {

    PostEntity createPost(PostEntity post, Long authorId);

    List<PostEntity> getAllPosts();

    List<PostEntity> getPostsByAuthor(Long authorId);

    PostEntity updatePost(Long id, PostEntity updatedPost);

    PostEntity getPostById(Long id);

    boolean deletePost(Long id);

    // Método para validar una publicación
    PostEntity validatePost(Long id);
}
