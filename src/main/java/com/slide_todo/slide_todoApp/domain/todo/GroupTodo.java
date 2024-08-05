package com.slide_todo.slide_todoApp.domain.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import jakarta.annotation.Nullable;
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

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "done_group_member_id")
  private GroupMember groupMember;

  @Builder
  public GroupTodo(String title, String linkUrl, Goal groupGoal) {
    super(title, linkUrl, groupGoal);
  }

  /**
   * 그룹 할 일의 완료 상태 변경
   */
  public void doneGroupTodo(GroupMember groupMember) {
    this.updateDone();
    if (this.getIsDone()) {
      this.groupMember = groupMember;
    } else {
      this.groupMember = null;
    }
  }
}
