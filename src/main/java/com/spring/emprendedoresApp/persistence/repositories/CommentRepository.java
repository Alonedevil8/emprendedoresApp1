package com.spring.emprendedoresApp.persistence.repositories;

import com.spring.emprendedoresApp.persistence.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    // Método para obtener una lista de comentarios de una publicación específica
    List<CommentEntity> findByPostId(Long postId);  // Obtener comentarios por el ID de la publicación

}
