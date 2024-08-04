package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByTitle(String title);

    Optional<Group>  findBySecretCode(String secretCode);

    @Query("SELECT gm FROM GroupMember gm LEFT JOIN FETCH gm.group WHERE gm.group.id = :groupId")
    List<GroupMember> findByGroupIdWithGroupMember(@Param("groupId") Long groupId);

    @Query("SELECT gg FROM GroupGoal gg LEFT JOIN FETCH  gg.group WHERE gg.group.id = :groupId")
    List<GroupGoal> findByGroupIdWithGroupGoal(@Param("groupId") Long groupId);

}
