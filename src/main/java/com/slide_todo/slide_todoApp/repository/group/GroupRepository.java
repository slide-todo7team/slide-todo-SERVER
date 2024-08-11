package com.slide_todo.slide_todoApp.repository.group;

import com.slide_todo.slide_todoApp.domain.group.Group;
//import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {
    Optional<Group> findByTitle(String title);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.groupMembers WHERE g.secretCode = :secretCode")
    Optional<Group>  findBySecretCode(String secretCode);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.groupMembers WHERE g.id = :groupId")
    Group findGroupWithGroupMembers(@Param("groupId") Long groupId);

    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.groupGoals WHERE g.id = :groupId")
    Group findGroupWithGroupGoals(@Param("groupId") Long groupId);

    /*GroupMember, GroupGoal을 한 번에 조회 하는 메소드*/
    @Query("SELECT g FROM Group g"
        + " LEFT JOIN FETCH g.groupMembers"
        + " LEFT JOIN FETCH g.groupGoals"
        + " WHERE g.id = :groupId")
    Group findGroupWithGroupMembersAndGroupGoals(@Param("groupId") Long groupId);

    Page<Group> findAll(Specification<Group> spec, Pageable pageable);

    List<Group> findAll(Specification<Group> spec);

    @Query("select g from Group g left join fetch g.groupGoals where g.id in :groupIds")
    List<Group> findAllByGroupIds(List<Long> groupIds);
}
