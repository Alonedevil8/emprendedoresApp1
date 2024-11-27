package com.spring.emprendedoresApp.services.impl;

import com.spring.emprendedoresApp.persistence.entities.PostEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.persistence.repositories.PostRepository;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;
import com.spring.emprendedoresApp.services.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PostEntity getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public PostEntity createPost(PostEntity post, Long authorId) {
        UserEntity author = userRepository.findById(authorId).orElse(null);

        if (author == null) {
            throw new RuntimeException("El autor con ID " + authorId + " no existe");
        }

        post.setAuthor(author);
        return postRepository.save(post);
    }

    @Override
    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<PostEntity> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }

    @Override
    public PostEntity updatePost(Long id, PostEntity updatedPost) {
        PostEntity existingPost = postRepository.findById(id).orElse(null);

        if (existingPost == null) {
            return null;
        }

        // Actualizamos únicamente los campos editables sin afectar los comentarios ni la fecha de creación
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setPostStatus(updatedPost.getPostStatus());
        existingPost.setPostType(updatedPost.getPostType());

        if (updatedPost.getValidationDate() != null) {
            existingPost.setValidationDate(updatedPost.getValidationDate());
        }

        // Retornamos la entidad actualizada, pero conservando los datos existentes como comentarios
        return postRepository.save(existingPost);
    }

    @Override
    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public PostEntity validatePost(Long id) {
        PostEntity post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setValido(true);  // Cambia el estado de validez
            post.setValidationDate(LocalDateTime.now());  // Asigna la fecha de validación
            return postRepository.save(post);  // Guarda la publicación actualizada
        }
        return null;
    }
}
