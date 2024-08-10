package com.slide_todo.slide_todoApp.domain.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("I")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndividualTodo extends Todo {

  @Builder
  public IndividualTodo(String title, Goal individualGoal, Member writer) {
    super(title, individualGoal, writer);
  }

  @Override
  public Boolean checkDone() {
    return this.getIsDone();
  }

  /**
   * 개인 할 일을 완료 처리
   */
  public void updateIndividualTodoDone() {
    this.updateDone();
  }
}
