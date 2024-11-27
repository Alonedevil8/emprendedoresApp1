package com.spring.emprendedoresApp.controllers;

import com.spring.emprendedoresApp.persistence.entities.PostEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.services.IPostService;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private IPostService postService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<PostEntity> createPost(@RequestBody PostEntity post) {
        Long authorId = post.getAuthor().getId();
        UserEntity author = userRepository.findById(authorId).orElse(null);
        
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        post.setAuthor(author);
        PostEntity createdPost = postService.createPost(post, authorId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostEntity>> getAllPosts() {
        List<PostEntity> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostEntity>> getPostsByAuthor(@PathVariable Long authorId) {
        List<PostEntity> posts = postService.getPostsByAuthor(authorId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostEntity> updatePost(@PathVariable Long id, @RequestBody PostEntity updatedPost) {
        PostEntity existingPost = postService.getPostById(id);

        if (existingPost == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setPostStatus(updatedPost.getPostStatus());
        existingPost.setPostType(updatedPost.getPostType());
        if (updatedPost.getValidationDate() != null) {
            existingPost.setValidationDate(updatedPost.getValidationDate());
        }

        PostEntity savedPost = postService.updatePost(id, updatedPost);
        return new ResponseEntity<>(savedPost, HttpStatus.OK);
    }

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
}
