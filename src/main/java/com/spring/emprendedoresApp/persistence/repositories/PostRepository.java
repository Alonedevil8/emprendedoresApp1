package com.spring.emprendedoresApp.persistence.repositories;

import com.spring.emprendedoresApp.persistence.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    // Método para obtener una lista de publicaciones por el ID del autor
    List<PostEntity> findByAuthorId(Long authorId);  // Obtener publicaciones por el ID del autor
    
 // Método para obtener publicaciones filtradas por postStatus
    Page<PostEntity> findByPostStatus(PostEntity.PostStatus postStatus, Pageable pageable);

}
