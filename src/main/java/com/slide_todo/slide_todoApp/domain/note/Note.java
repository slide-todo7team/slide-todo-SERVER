package com.slide_todo.slide_todoApp.domain.note;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
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

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "todo_id")
  private Todo todo;

  @Length(max = 30)
  @Size(max = 30)
  private String title;

  @Length(max = 10000)
  @Size(max = 10000)
  private String content;

  @Nullable
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
  public Note(Todo todo, String title, String content, @Nullable String linkUrl) {
    this.title = title;
    this.content = content;
    this.linkUrl = linkUrl;
    this.isDeleted = false;
    this.updatedAt = LocalDateTime.now();

    /*연관 관계 편의 메소드*/
    this.todo = todo;
    this.todo.writeNote(this);
  }

  /**
   * 할 일의 노트를 수정
   * @param title
   * @param content
   */
  public void updateNote(String title, String content, @Nullable String linkUrl) {
    if (title != null) {
      this.title = title;
    }
    if (content != null) {
      this.content = content;
    }
    this.linkUrl = linkUrl;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 할 일의 노트를 삭제
   */
  public void deleteNote() {
    this.getTodo().deleteNote();
    this.isDeleted = true;
    this.updatedAt = LocalDateTime.now();
  }
}
