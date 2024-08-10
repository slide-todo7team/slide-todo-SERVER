package com.slide_todo.slide_todoApp.domain.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.note.Note;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AccessLevel;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@SQLRestriction("is_deleted = false")
public abstract class Todo {

  @Id
  @Column(name = "todo_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Length(max = 30)
  @Size(max = 30)
  private String title;
  private Boolean isDeleted;
  private Boolean isDone;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "writer")
  private Member writer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "goal_id")
  private Goal goal;

  @Nullable
  @OneToOne(mappedBy = "todo", fetch = FetchType.LAZY)
  private Note note;


  @CreatedDate
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime updatedAt;

  public Todo(String title, Goal goal, Member writer) {
    this.title = title;
    this.isDeleted = false;
    this.isDone = false;
    this.writer = writer;
    this.goal = goal;
    this.goal.getTodos().add(this);
    this.updatedAt = LocalDateTime.now();
  }

  @Transient
  public String getDtype() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
  }

  /**
   * 노트를 생성
   *
   * @param note
   */
  public void writeNote(Note note) {
    this.note = note;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 할 일 삭제 처리<br> 노트가 등록 되어 있을 시 노트도 삭제합니다.
   */
  public void deleteTodo() {
    this.isDeleted = true;
    if (this.note != null) {
      this.note.deleteNote();
    }
    this.getGoal().getTodos().remove(this);
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 할 일의 제목과 링크를 수정.
   *
   * @param title
   */
  public void updateTodo(String title) {
    if (title != null) {
      this.title = title;
    }
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 할 일의 완료 상태 변경
   */
  void updateDone() {
    this.isDone = !this.isDone;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 할 일의 완료 여부를 확인
   *
   * @return
   */
  public Boolean checkDone() {
    return this.isDone;
  }

  /**
   * 할 일의 노트를 삭제
   */
  public void deleteNote() {
    this.note = null;
  }
}
