package com.slide_todo.slide_todoApp.repository.goal;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupGoalRepository extends JpaRepository<GroupGoal,Long> {
    List<GroupGoal> findAllByGroup(Group group);

   

    Long countByGroup(Group group);

    GroupGoal findByGroupAndId(Group group, Long goalId);

    List<GroupGoal> findAllByGroup(Group group,Pageable pageable);
    @Query("SELECT gg FROM GroupGoal gg LEFT JOIN FETCH gg.todos"
        + " LEFT JOIN FETCH gg.group WHERE gg.id = :goalId")
    GroupGoal findGroupGoalWithTodos(@Param("goalId") Long goalId);
}
