package com.slide_todo.slide_todoApp.domain.group;

import com.slide_todo.slide_todoApp.domain.member.Member;
import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "memberInCharge", fetch = FetchType.LAZY)
    private List<GroupTodo> chargingTodos = new ArrayList<>();

    private Boolean isLeader;
    private Boolean isDeleted=false;

    private Integer todoCount;

    @Enumerated(EnumType.STRING)
    private ColorEnum color;

    @CreationTimestamp
    @Column(name = "registered_at", updatable = false)
    private LocalDateTime registeredAt;

    @Builder
    public GroupMember(Member member, Group group, Boolean isLeader, ColorEnum color) {
        this.member = member;
        this.group = group;
        this.isLeader = isLeader;
        this.todoCount = 0;
        this.color = color;
        this.isDeleted = false;
    }

    public void increaseTodoCount() {
        this.todoCount++;
    }

    public void decreaseTodoCount() {
        this.todoCount--;
    }

    public void changeColor(ColorEnum color) {
        this.color = color;
    }

    public void updateIsLeader(Boolean isLeader) {
        this.isLeader = isLeader;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setIsLeader(Boolean isLeader) {
        this.isLeader = isLeader;
    }

    public void setTodoCount(int i) {
        this.todoCount = i;
    }

    public void setColor(ColorEnum randomColor) {
        this.color = randomColor;
    }

    public void deleteGroupMember() {
        this.getGroup().getGroupMembers().remove(this);
        this.getChargingTodos().forEach(o -> {
            o.updateMemberInCharge(this);
        });
        this.isDeleted = true;
    }
}
