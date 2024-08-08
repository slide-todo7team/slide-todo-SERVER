package com.slide_todo.slide_todoApp.domain.member;

import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("is_deleted = false")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @OneToMany(mappedBy = "member")
  @BatchSize(size = 10)
  private List<IndividualGoal> individualGoals = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  @BatchSize(size = 10)
  private List<GroupMember> groupMembers = new ArrayList<>();

  private String email;
  private String password;
  private String nickname;
  private Boolean isDeleted;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  @CreatedDate
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @Builder
  public Member(String email, String password, String nickname) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.role = MemberRole.USER;
    this.updatedAt = LocalDateTime.now();
    this.isDeleted = false;
  }

  public void updateMember(String email, String nickname) {
    if (email != null) {this.email = email;}
    if (nickname != null) {this.nickname = nickname;} // 서비스 로직에서 닉네임 중복 여부 확인 필요
    this.updatedAt = LocalDateTime.now();
  }

  public void deleteMember() {
    this.getIndividualGoals().forEach(IndividualGoal::deleteGoal);
    this.getGroupMembers().forEach(GroupMember::deleteGroupMember);
    this.updatedAt = LocalDateTime.now();
    this.isDeleted = true;
  }

  public void updateGroupMembers(List<GroupMember> groupMembers) {
    this.groupMembers = groupMembers;
  }
}
