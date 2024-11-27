package com.spring.emprendedoresApp.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "posts")
public class PostEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "title", nullable = false, length = 255)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "creation_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime creationDate;

	@Column(name = "validation_date")
	private LocalDateTime validationDate;

	@Column(name = "is_valido", nullable = false)
	private boolean isValido; // Nuevo campo para marcar la validez de la publicación

	@Enumerated(EnumType.STRING)
	@Column(name = "post_status", nullable = false)
	private PostStatus postStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "post_type", nullable = false)
	private PostType postType;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	private UserEntity author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@JsonManagedReference("post-comments") // Serializa los comentarios desde la perspectiva de la publicación
	private Set<CommentEntity> comments;

	public enum PostStatus {
		IDEAS, STORIES, INVESTMENT, ADVICE, RESOURCES, OTHERS
	}

	public enum PostType {
		PUBLISHED, PENDING, REJECTED
	}

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

	// ----------- Getters y Setters -----------

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
