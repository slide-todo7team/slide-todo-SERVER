package com.slide_todo.slide_todoApp.domain.note;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("is_deleted = false")
public class Note {

  @Id
  @Column(name = "note_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "todo_id")
  private Todo todo;

  private String title;
  @Lob
  private String content;
  private Boolean isDeleted;

  @CreatedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;
  @LastModifiedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;

  @Builder
  public Note(Todo todo, String title, String content) {
    this.title = title;
    this.content = content;
    this.isDeleted = false;

    /*연관 관계 편의 메소드*/
    this.todo = todo;
    this.todo.writeNote(this);
  }

  public void updateNote(String title, String content) {
    if (title != null) {
      this.title = title;
    }
    if (content != null) {
      this.content = content;
    }
  }

  public void deleteNote() {
    this.isDeleted = true;
  }
}
