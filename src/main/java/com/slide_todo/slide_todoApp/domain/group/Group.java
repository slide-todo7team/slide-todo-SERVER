package com.slide_todo.slide_todoApp.domain.group;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.dto.group.GroupCreateDTO;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import com.slide_todo.slide_todoApp.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`group`")
@SQLRestriction("is_deleted = false")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    //그룹을 생성한 사람
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "created_group_member_id") //그룹을 생성한 사람
    private GroupMember createdGroupMember;

    // Group 생성 시 createdGroupMember도 함께 생성하도록 PERSIST 추가
    // Group 삭제 시 groupMember 모두 삭제되도록 REMOVE 추가
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<GroupMember> groupMembers = new ArrayList<>();

    // Group 삭제 시 groupGoal 모두 삭제되도록 REMOVE 추가
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE)
    private List<GroupGoal> groupGoals = new ArrayList<>();

    private String title;

    @Column(name = "secret_code")
    private String secretCode;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Boolean isDeleted=false;

    @Builder
    public Group(GroupCreateDTO groupCreateDTO, Member member) {
        this.title = groupCreateDTO.getTitle();
        this.secretCode = groupCreateDTO.getSecretCode();

        // 그룹 생성 시 createdGroupMember도 함께 생성
        this.createdGroupMember = GroupMember.builder()
                .member(member)
                .group(this)
                .isLeader(true)
                .color(ColorEnum.COLOR_1)
                .build();
        this.groupMembers.add(createdGroupMember);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    // createdGroupMember가 그룹장 역할이라면 기존 그룹장 계정 삭제 시 그룹장 변경
    public void updateCreatedGroupMember(GroupMember groupMember) {
        this.createdGroupMember.updateIsLeader(false);
        this.createdGroupMember = groupMember;
        groupMember.updateIsLeader(true);
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public void setCreatedGroupMember(GroupMember groupMember) {
        this.createdGroupMember = groupMember;
    }

    public void deleteGroup() {
        this.isDeleted=true;
        List<GroupMember> groupMembers = this.groupMembers;

        for (GroupMember groupMember : new ArrayList<>(groupMembers)) {
            groupMember.deleteGroupMember();
            groupMembers.remove(groupMember);
        }

        List<GroupGoal> groupGoals = this.groupGoals;
        for (GroupGoal groupGoal : new ArrayList<>(groupGoals)) {
            groupGoal.deleteGroupGoal();
            groupGoals.remove(groupGoal);
        }
    }

}
