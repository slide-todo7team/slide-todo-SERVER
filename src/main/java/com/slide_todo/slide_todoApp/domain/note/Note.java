package com.slide_todo.slide_todoApp.domain.note;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
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

  @Length(max = 30)
  @Size(max = 30)
  private String title;
  @Length(max = 10000)
  @Size(max = 10000)
  private String content;
  @Length(max = 255)
  @Size(max = 255)
  private String linkUrl;
  private Boolean isDeleted;

  @CreatedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;

  @Builder
  public Note(Todo todo, String title, String content, String linkUrl) {
    this.title = title;
    this.content = content;
    this.linkUrl = linkUrl;
    this.isDeleted = false;
    this.updatedAt = LocalDateTime.now();

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
    this.updatedAt = LocalDateTime.now();
  }

  public void deleteNote() {
    this.isDeleted = true;
    this.updatedAt = LocalDateTime.now();
  }
}
