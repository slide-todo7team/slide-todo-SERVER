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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
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

  @OneToOne
  @JoinColumn(name = "modified_group_member_id")
  private GroupMember modifiedGroupMember;

  private String title;
  @Lob
  private String content;
  private String linkUrl;
  private Boolean isDeleted;

  @CreatedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;

  @Builder
  public Note(Todo todo, String title, String content, String linkUrl, GroupMember modifiedGroupMember) {
    this.title = title;
    this.content = content;
    this.linkUrl = linkUrl;
    this.isDeleted = false;
    this.modifiedGroupMember = modifiedGroupMember;
    this.updatedAt = LocalDateTime.now();

    /*연관 관계 편의 메소드*/
    this.todo = todo;
    this.todo.writeNote(this);
  }

  public void updateNote(String title, String content, GroupMember modifiedGroupMember) {
    if (title != null) {
      this.title = title;
    }
    if (content != null) {
      this.content = content;
    }
    this.modifiedGroupMember = modifiedGroupMember;
    this.updatedAt = LocalDateTime.now();
  }

  public void deleteNote() {
    this.isDeleted = true;
    this.updatedAt = LocalDateTime.now();
  }
}
