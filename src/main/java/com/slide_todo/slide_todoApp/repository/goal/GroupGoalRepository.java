package com.slide_todo.slide_todoApp.repository.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.dto.goal.GroupGoalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface GroupGoalRepository extends JpaRepository<GroupGoal,Long> {
    List<GroupGoal> findAllByGroup(Group group);

    List<GroupGoal> findAllByGroupAndIdGreaterThan(Group group, Long cursor, Pageable of);

    Long countByGroup(Group group);

    GroupGoal findByGroupAndId(Group group, Long goalId);

    @Query("SELECT gg FROM GroupGoal gg LEFT JOIN FETCH gg.todos WHERE gg.id = :goalId")
    GroupGoal findGroupGoalWithTodos(@Param("goalId") Long goalId);
}
