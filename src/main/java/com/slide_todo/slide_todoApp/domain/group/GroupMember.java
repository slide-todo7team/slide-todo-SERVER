package com.slide_todo.slide_todoApp.domain.group;

import com.slide_todo.slide_todoApp.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    private Boolean isLeader;

    private Integer todoCount;

    @Enumerated(EnumType.STRING)
    private ColorEnum color;

    @Builder
    public GroupMember(Member member, Group group, Boolean isLeader, ColorEnum color) {
        this.member = member;
        this.group = group;
        this.isLeader = isLeader;
        this.todoCount = 0;
        this.color = color;
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
    ////
    public String getName() {
        return member.getName();
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
}
