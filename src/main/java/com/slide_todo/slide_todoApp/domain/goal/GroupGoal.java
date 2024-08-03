package com.slide_todo.slide_todoApp.domain.goal;

import com.slide_todo.slide_todoApp.domain.group.Group;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("G")
public class GroupGoal extends Goal {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

}
