package com.spring.emprendedoresApp.persistence.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    
    @NotNull(message = "El Comentario no puede ser nulo")
    @Size(min = 1, max = 255, message = "El Comentario debe tener entre 1 y 255 caracteres")
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-comments")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference("post-comments")
    private PostEntity post; 

    @Column(name = "creation_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @PrePersist
    public void prePersist() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now();
        }
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }
}
