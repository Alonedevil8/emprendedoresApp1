package com.spring.emprendedoresApp.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "posts")
public class PostEntity {

    // ---------- Atributos de la entidad ----------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @NotNull(message = "El título no puede ser nulo")
    @Size(min = 1, max = 255, message = "El título debe tener entre 1 y 255 caracteres")
    @Column(name = "title")
    private String title;

    @NotNull(message = "El contenido no puede ser nulo")
    @Size(min = 1, message = "El contenido no puede estar vacío")
    @Column(name = "content")
    private String content;

    @PastOrPresent(message = "La fecha de creación debe ser en el pasado o presente")
    @Column(name = "creation_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime creationDate;

    @PastOrPresent(message = "La fecha de validación debe ser en el pasado o presente")
    @Column(name = "validation_date")
    private LocalDateTime validationDate;

    @NotNull(message = "El campo de validez no puede ser nulo")
    @Column(name = "is_valido")
    private boolean isValido;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado de la publicación no puede ser nulo")
    @Column(name = "post_status")
    private PostStatus postStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NotNull(message = "El autor no puede ser nulo")
    private UserEntity author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference("post-comments") // Serializa los comentarios desde la perspectiva de la publicación
    private Set<CommentEntity> comments;

    // ---------- Enumeraciones ----------

    public enum PostStatus {
        IDEAS, STORIES, INVESTMENT, ADVICE, RESOURCES, OTHERS
    }

    public enum PostType {
        PUBLISHED, PENDING, REJECTED
    }

    // ---------- Métodos del ciclo de vida ----------

    /**
     * Método ejecutado antes de persistir una nueva entidad.
     * Configura valores predeterminados para ciertos campos.
     */
    @PrePersist
    public void prePersist() {
        if (this.creationDate == null) {
            this.creationDate = LocalDateTime.now();
        }
        if (this.postStatus == null) {
            this.postStatus = PostStatus.OTHERS;
        }
        if (this.postType == null) {
            this.postType = PostType.PENDING;
        }
        if (!this.isValido) {
            this.isValido = false;
        }
    }

    /**
     * Método ejecutado antes de actualizar una entidad existente.
     * Realiza validaciones y actualiza valores específicos.
     */
    @PreUpdate
    public void preUpdate() {
        // Validar que la fecha de validación no sea anterior a la de creación
        if (this.validationDate != null && this.validationDate.isBefore(this.creationDate)) {
            throw new IllegalArgumentException("La fecha de validación no puede ser anterior a la fecha de creación.");
        }

        // Si el estado es publicado, asegurarse de que sea válido
        if (this.postType == PostType.PUBLISHED && !this.isValido) {
            throw new IllegalArgumentException("Una publicación marcada como PUBLISHED debe ser válida.");
        }

        // Actualizar automáticamente la fecha de validación si está en blanco
        if (this.isValido && this.validationDate == null) {
            this.validationDate = LocalDateTime.now();
        }
    }

    // ---------- Getters y Setters ----------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(LocalDateTime validationDate) {
        this.validationDate = validationDate;
    }

    public boolean isValido() {
        return isValido;
    }

    public void setValido(boolean isValido) {
        this.isValido = isValido;
    }

    public PostStatus getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public Set<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(Set<CommentEntity> comments) {
        this.comments = comments;
    }
}
