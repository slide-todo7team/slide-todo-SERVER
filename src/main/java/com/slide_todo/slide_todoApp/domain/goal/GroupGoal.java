package com.slide_todo.slide_todoApp.domain.goal;

import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("G")
public class GroupGoal extends Goal {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    // 해당 목표를 생성한 그룹 멤버
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id")
    private GroupMember groupMember;

    @Builder
    public GroupGoal(String title, Group group, GroupMember groupMember) {
        super(title);
        this.group = group;
        this.groupMember = groupMember;
    }

    public GroupGoal() {
        super();
    }

    public void deleteGroupGoal() {
        this.deleteGoal();
        this.getGroup().getGroupGoals().remove(this);
    }
}
