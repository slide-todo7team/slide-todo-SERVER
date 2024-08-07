package com.slide_todo.slide_todoApp.repository.goal;

import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IndividualGoalRepository extends JpaRepository<IndividualGoal,Long> {
    List<IndividualGoal> findAllByMember(Member member);

    Long countByMember(Member member);
    List<IndividualGoal> findAllByMember(Member member, Pageable pageable);

    @Query("SELECT ig FROM IndividualGoal ig LEFT JOIN FETCH ig.todos WHERE ig.id = :goalId")
    IndividualGoal findIndividualGoalWithTodos(@Param("goalId") Long goalId);
}
