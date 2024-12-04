package com.spring.emprendedoresApp.controllers;

import com.spring.emprendedoresApp.persistence.entities.PostEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.services.IPostService;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private UserRepository userRepository;

    // Crear una publicación con validación de los campos
    @PostMapping("/create")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostEntity post, BindingResult result) {
        if (result.hasErrors()) {
            // Si hay errores de validación, se devuelven los errores al cliente
            StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Long authorId = post.getAuthor().getId();
        UserEntity author = userRepository.findById(authorId).orElse(null);

        if (author == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        post.setAuthor(author);
        PostEntity createdPost = postService.createPost(post, authorId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // Obtener todas las publicaciones
    @GetMapping
    public ResponseEntity<List<PostEntity>> getAllPosts() {
        List<PostEntity> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // Obtener publicaciones por autor
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostEntity>> getPostsByAuthor(@PathVariable Long authorId) {
        List<PostEntity> posts = postService.getPostsByAuthor(authorId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

 // Modificación en el controlador
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody PostEntity updatedPost, BindingResult result) {
        if (result.hasErrors()) {
            // Si hay errores de validación, se devuelven los errores al cliente en un formato adecuado
            StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            // Aquí devolvemos un ResponseEntity con un mensaje de error en lugar de una entidad PostEntity
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        // Buscar la publicación existente
        PostEntity existingPost = postService.getPostById(id);
        if (existingPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Actualizar la publicación
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setPostStatus(updatedPost.getPostStatus());
        existingPost.setPostType(updatedPost.getPostType());
        if (updatedPost.getValidationDate() != null) {
            existingPost.setValidationDate(updatedPost.getValidationDate());
        }

        // Guardar la publicación actualizada
        PostEntity savedPost = postService.updatePost(id, updatedPost);
        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }


    // Eliminar una publicación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        boolean isDeleted = postService.deletePost(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para validar una publicación
    @PatchMapping("/validate/{id}")
    public ResponseEntity<PostEntity> validatePost(@PathVariable Long id) {
        PostEntity validatedPost = postService.validatePost(id);
        if (validatedPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(validatedPost, HttpStatus.OK);
    }
    
    // Endpoint para obtener publicaciones filtradas por postStatus y paginadas
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PostEntity>> getPostsByStatus(
            @PathVariable PostEntity.PostStatus status,  // El valor del postStatus se pasa como parámetro en la URL
            @RequestParam(defaultValue = "0") int page,   // Página actual (default 0)
            @RequestParam(defaultValue = "10") int size)  // Tamaño de la página (default 10)
    {
        Page<PostEntity> posts = postService.getPostsByStatus(status, page, size);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
