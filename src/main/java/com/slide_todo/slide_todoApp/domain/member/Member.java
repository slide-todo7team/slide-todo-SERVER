package com.slide_todo.slide_todoApp.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  private String email;
  private String password;
  private String name;
  private String nickname;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  @CreatedDate
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Member(String email, String password, String name, String nickname, MemberRole role) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.nickname = nickname;
    this.role = role;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateMember(String email, String name, String nickname) {
    if (email != null) {this.email = email;}
    if (name != null) {this.name = name;}
    if (nickname != null) {this.nickname = nickname;} // 서비스 로직에서 닉네임 중복 여부 확인 필요
    this.updatedAt = LocalDateTime.now();
  }
}
