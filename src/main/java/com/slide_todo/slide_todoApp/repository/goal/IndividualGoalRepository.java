package com.slide_todo.slide_todoApp.repository.goal;

import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IndividualGoalRepository extends JpaRepository<IndividualGoal,Long> {
    List<IndividualGoal> findAllByMember(Member member);
    List<IndividualGoal> findAllByMemberAndIdGreaterThan(Member member, Long cursor, Pageable pageable);
    Long countByMember(Member member);
}
