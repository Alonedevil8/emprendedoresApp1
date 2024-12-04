package com.spring.emprendedoresApp.services;

import com.spring.emprendedoresApp.persistence.entities.PostEntity;
import com.spring.emprendedoresApp.persistence.entities.PostEntity.PostStatus;

import java.util.List;

import org.springframework.data.domain.Page;

public interface IPostService {

    // Crear una publicación
    PostEntity createPost(PostEntity post, Long authorId);

    // Obtener todas las publicaciones
    List<PostEntity> getAllPosts();

    // Obtener publicaciones por autor
    List<PostEntity> getPostsByAuthor(Long authorId);

    // Actualizar una publicación
    PostEntity updatePost(Long id, PostEntity updatedPost);

    // Obtener una publicación por ID
    PostEntity getPostById(Long id);

    // Eliminar una publicación
    boolean deletePost(Long id);

    // Validar una publicación
    PostEntity validatePost(Long id);

	Page<PostEntity> getPostsByStatus(PostStatus postStatus, int page, int size);
}
