package com.spring.emprendedoresApp.services.impl;

import com.spring.emprendedoresApp.persistence.entities.CommentEntity;
import com.spring.emprendedoresApp.persistence.repositories.CommentRepository;
import com.spring.emprendedoresApp.persistence.repositories.PostRepository;
import com.spring.emprendedoresApp.services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {

    // Inyección de dependencias para los repositorios necesarios
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    // Método para crear un nuevo comentario
    @Override
    public CommentEntity createComment(CommentEntity comment, Long postId) {
        // Asigna la publicación asociada al comentario buscando por ID
        // Si no se encuentra la publicación, se asigna 'null' (puedes manejarlo de forma distinta si lo prefieres)
        comment.setPost(postRepository.findById(postId).orElse(null)); 
        
        // Guarda el comentario en la base de datos y lo retorna
        return commentRepository.save(comment);
    }

    // Método para obtener todos los comentarios
    @Override
    public List<CommentEntity> getAllComments() {
        // Devuelve todos los comentarios almacenados en la base de datos
        return commentRepository.findAll();
    }

    // Método para obtener los comentarios de una publicación específica por su ID
    @Override
    public List<CommentEntity> getCommentsByPost(Long postId) {
        // Llama al repositorio para obtener los comentarios filtrados por el ID de la publicación
        return commentRepository.findByPostId(postId);
    }

    // Método para actualizar un comentario existente
    @Override
    public CommentEntity updateComment(Long id, CommentEntity updatedComment) {
        // Verifica si el comentario con el ID proporcionado existe en la base de datos
        if (commentRepository.existsById(id)) {
            // Si existe, asigna el ID del comentario actualizado
            updatedComment.setId(id);
            // Guarda y retorna el comentario actualizado
            return commentRepository.save(updatedComment);
        }
        // Si el comentario no existe, retorna null (o podrías lanzar una excepción, si lo prefieres)
        return null;
    }

    // Método para eliminar un comentario por su ID
    @Override
    public boolean deleteComment(Long id) {
        // Verifica si el comentario con el ID proporcionado existe en la base de datos
        if (commentRepository.existsById(id)) {
            // Si existe, elimina el comentario
            commentRepository.deleteById(id);
            // Retorna true para indicar que la eliminación fue exitosa
            return true;
        }
        // Si no existe, retorna false (el comentario no pudo ser eliminado)
        return false;
    }
}
