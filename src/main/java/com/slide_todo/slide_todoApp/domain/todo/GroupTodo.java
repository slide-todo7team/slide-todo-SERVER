package com.slide_todo.slide_todoApp.domain.todo;

import com.slide_todo.slide_todoApp.domain.goal.Goal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.util.exception.CustomException;
import com.slide_todo.slide_todoApp.util.exception.Exceptions;
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
  @JoinColumn(name = "charged_group_member_id")
  private GroupMember memberInCharge;

  @Builder
  public GroupTodo(String title, String content, Goal groupGoal) {
    super(title, content, groupGoal);
  }

  /**
   * 그룹 할 일의 담당자 변경
   */
  public void updateMemberInCharge(GroupMember memberInCharge) {
    if (this.memberInCharge == null) {
      this.memberInCharge = memberInCharge;
      memberInCharge.getChargingTodos().add(this);
    } else {
      if (this.memberInCharge.getId().equals(memberInCharge.getId())) {
        this.memberInCharge = null;
        memberInCharge.getChargingTodos().remove(this);
      } else {
        throw new CustomException(Exceptions.ALREADY_CHARGED_TODO);
      }
    }
  }

  /**
   * 그룹 할 일을 완료 처리
   */
  public void updateGroupTodoDone() {
    this.updateDone();
  }
}
