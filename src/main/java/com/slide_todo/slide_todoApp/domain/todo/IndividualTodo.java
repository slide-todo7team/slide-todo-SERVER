package com.slide_todo.slide_todoApp.domain.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("I")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualTodo extends Todo {


  private Boolean isDone;

  @Builder
  public IndividualTodo(String title, String linkUrl, Goal individualGoal) {
    super(title, linkUrl, individualGoal);
    this.isDone = false;
  }

  public void doneTodo() {
    this.isDone = true;
  }

  public Boolean checkDone() {
    return this.isDone;
  }
}
