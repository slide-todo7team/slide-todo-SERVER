package com.slide_todo.slide_todoApp.domain.todo;

import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
public abstract class Todo {

  @Id
  @Column(name = "todo_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String linkUrl;
  private Boolean isDeleted;

  @CreatedDate
  private LocalDateTime createdAt;

  public Todo(String title, String linkUrl) {
    this.title = title;
    this.linkUrl = linkUrl;
  }

  /**
   * 할 일 삭제 처리
   */
  public void deleteTodo() {
    this.isDeleted = true;
  }

  /**
   * 할 일의 제목과 링크를 수정.
   * @param title
   * @param linkUrl
   */
  public void updateTodo(String title, String linkUrl) {
    if (title != null) {this.title = title;}
    if (linkUrl != null) {this.linkUrl = linkUrl;}
  }

  /**
   * 할 일을 완료 처리
   * @return
   */
  public abstract Boolean checkDone();
}
