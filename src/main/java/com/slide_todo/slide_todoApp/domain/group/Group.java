package com.slide_todo.slide_todoApp.domain.group;

import com.slide_todo.slide_todoApp.dto.group.GroupCreateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.slide_todo.slide_todoApp.domain.member.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`group`")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "create_member_id") //그룹을 생성한 사람
    private Member member;

    private String title;

    @Column(name = "secret_code")
    private String secretCode;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Group(GroupCreateDTO groupCreateDTO, Member member) {
        this.title = groupCreateDTO.getTitle();
        this.secretCode = groupCreateDTO.getSecretCode();
        this.member = member;
    }


    @Builder
    public Group(Member createdMember, String title, String secretCode) {
        this.member = createdMember;
        this.title = title;
        this.secretCode = secretCode;
    }
}
