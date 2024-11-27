package com.spring.emprendedoresApp.persistence.repositories;

import com.spring.emprendedoresApp.persistence.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    // Método para obtener una lista de publicaciones por el ID del autor
    List<PostEntity> findByAuthorId(Long authorId);  // Obtener publicaciones por el ID del autor

}
