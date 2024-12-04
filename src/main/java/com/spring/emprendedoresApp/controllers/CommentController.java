package com.spring.emprendedoresApp.controllers;

import com.spring.emprendedoresApp.persistence.entities.CommentEntity;
import com.spring.emprendedoresApp.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")  // Ruta base para todas las solicitudes relacionadas con comentarios
public class CommentController {

    @Autowired
    private ICommentService commentService;  // Inyección del servicio de comentarios

    // Endpoint para crear un nuevo comentario
    @PostMapping("/create")  // Ruta para crear un comentario con la publicación en el cuerpo
    public ResponseEntity<?> create(@Valid @RequestBody CommentEntity comment, BindingResult result) {
        if (result.hasErrors()) {
            // Si hay errores de validación, se devuelven los errores al cliente
            StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        Long postId = comment.getPost().getId();
        CommentEntity createdComment = commentService.createComment(comment, postId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los comentarios
    @GetMapping  // Ruta para obtener todos los comentarios
    public ResponseEntity<List<CommentEntity>> getAllComments() {
        List<CommentEntity> comments = commentService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // Endpoint para obtener los comentarios de una publicación específica
    @GetMapping("/post/{postId}")  // Ruta para obtener comentarios asociados a una publicación por su ID
    public ResponseEntity<List<CommentEntity>> getCommentsByPost(@PathVariable Long postId) {
        List<CommentEntity> comments = commentService.getCommentsByPost(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // Endpoint para actualizar un comentario existente
    @PutMapping("/{id}")  // Ruta para actualizar un comentario existente por su ID
    public ResponseEntity<CommentEntity> updateComment(@PathVariable Long id, @RequestBody CommentEntity updatedComment) {
        CommentEntity updated = commentService.updateComment(id, updatedComment);
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para eliminar un comentario
    @DeleteMapping("/{id}")  // Ruta para eliminar un comentario por su ID
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        boolean isDeleted = commentService.deleteComment(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
