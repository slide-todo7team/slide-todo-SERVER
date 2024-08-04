package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
//import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByTitle(String title);

    Optional<Group>  findBySecretCode(String secretCode);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.members WHERE g.id = :groupId")
    Group findGroupWithMembers(@Param("groupId") Long groupId);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.groupGoals WHERE g.id = :groupId")
    Group findGroupWithGoals(@Param("groupId") Long groupId);

}
