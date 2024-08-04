package com.slide_todo.slide_todoApp.domain.todo;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "individual_goal_id")
  private IndividualGoal individualGoal;

  private Boolean isDone;

  @Builder
  public IndividualTodo(String title, String linkUrl, IndividualGoal individualGoal) {
    super(title, linkUrl);
    this.isDone = false;

    /*연관 관계 편의 메소드*/
    this.individualGoal = individualGoal;
    this.individualGoal.getIndividualTodos().add(this);
  }

  public void doneTodo() {
    this.isDone = true;
  }

  public Boolean checkDone() {
    return this.isDone;
  }
}
