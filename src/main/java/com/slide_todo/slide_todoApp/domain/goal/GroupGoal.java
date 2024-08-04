package com.slide_todo.slide_todoApp.domain.goal;

import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("G")
public class GroupGoal extends Goal {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "groupGoal", fetch = FetchType.LAZY)
    private List<GroupTodo> groupTodos = new ArrayList<>();
}
