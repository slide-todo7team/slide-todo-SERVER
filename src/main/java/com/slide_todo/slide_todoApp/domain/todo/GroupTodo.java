package com.slide_todo.slide_todoApp.domain.todo;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
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
@DiscriminatorValue("G")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupTodo extends Todo {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "done_group_member_id")
  private GroupMember groupMember;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "group_goal_id")
  private GroupGoal groupGoal;

  @Builder
  public GroupTodo(String title, String linkUrl, GroupGoal groupGoal) {
    super(title, linkUrl);

    /*연관 관계 편의 메소드*/
    this.groupGoal = groupGoal;
    this.groupGoal.getGroupTodos().add(this);
  }

  public void doneTodo(GroupMember groupMember) {
    this.groupMember = groupMember;
  }

  @Override
  public Boolean checkDone() {
    return this.groupMember != null;
  }
}
