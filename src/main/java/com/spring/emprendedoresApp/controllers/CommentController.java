package com.spring.emprendedoresApp.controllers;

import com.spring.emprendedoresApp.persistence.entities.CommentEntity;
import com.spring.emprendedoresApp.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")  // Ruta base para todas las solicitudes relacionadas con comentarios
public class CommentController {

    @Autowired
    private ICommentService commentService;  // Inyección del servicio de comentarios

    // Endpoint para crear un nuevo comentario
    @PostMapping("/{postId}")  // Ruta para crear un comentario y asociarlo a una publicación
    public ResponseEntity<CommentEntity> createComment(@RequestBody CommentEntity comment, @PathVariable Long postId) {
        // Llamada al servicio para crear un comentario y asociarlo con la publicación
        CommentEntity createdComment = commentService.createComment(comment, postId);
        // Retorna el comentario creado con el código de estado 201 (CREATED)
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los comentarios
    @GetMapping  // Ruta para obtener todos los comentarios
    public ResponseEntity<List<CommentEntity>> getAllComments() {
        // Llamada al servicio para obtener todos los comentarios
        List<CommentEntity> comments = commentService.getAllComments();
        // Retorna la lista de comentarios con el código de estado 200 (OK)
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // Endpoint para obtener los comentarios de una publicación específica
    @GetMapping("/post/{postId}")  // Ruta para obtener comentarios asociados a una publicación por su ID
    public ResponseEntity<List<CommentEntity>> getCommentsByPost(@PathVariable Long postId) {
        // Llamada al servicio para obtener comentarios de una publicación específica
        List<CommentEntity> comments = commentService.getCommentsByPost(postId);
        // Retorna los comentarios de la publicación con el código de estado 200 (OK)
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // Endpoint para actualizar un comentario existente
    @PutMapping("/{id}")  // Ruta para actualizar un comentario existente por su ID
    public ResponseEntity<CommentEntity> updateComment(@PathVariable Long id, @RequestBody CommentEntity updatedComment) {
        // Llamada al servicio para actualizar el comentario
        CommentEntity updated = commentService.updateComment(id, updatedComment);
        // Si el comentario se actualiza correctamente, retorna el comentario actualizado con código 200 (OK)
        // Si no se encuentra el comentario, retorna código 404 (NOT FOUND)
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para eliminar un comentario
    @DeleteMapping("/{id}")  // Ruta para eliminar un comentario por su ID
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        // Llamada al servicio para eliminar el comentario
        boolean isDeleted = commentService.deleteComment(id);
        // Si el comentario se elimina exitosamente, retorna código 204 (NO CONTENT)
        // Si no se encuentra el comentario, retorna código 404 (NOT FOUND)
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
