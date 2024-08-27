package com.slide_todo.slide_todoApp.dto.group;

import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.Group;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
public class GroupInfoDTO {
    private Long id;
    private String title;
    private String createUser;
    private String secretCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GroupMemberDTO> members;
    private List<GoalDTO> goals;

    @Data
    public static class GoalDTO{
        private Long id;
        private String title;
        private BigDecimal progress;

        public GoalDTO(GroupGoal groupGoal) {
            this.id = groupGoal.getId();
            this.title = groupGoal.getTitle();
            this.progress = groupGoal.getProgressRate();
        }
    }


    @Getter
    @Setter
    @Builder
    public static class GroupMemberDTO {
        private Long id;
        private Boolean isLeader;
        private String nickname;
        private String color;
        private Integer contributionRank;
    }

    public GroupInfoDTO(Group group) {
        this.id = group.getId();
        this.title = group.getTitle();
        this.createUser = group.getCreatedGroupMember().getMember().getNickname();
        this.secretCode = group.getSecretCode();
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getUpdatedAt();
    }

    public void calculateContributionRank(List<GroupMember> groupMembers) {
        // todoCount를 기준으로 정렬하여 순위 매기기
        List<GroupMember> sortedMembers = groupMembers.stream()
                .sorted(Comparator.comparingInt(GroupMember::getTodoCount).reversed())
                .toList();

        List<GroupMemberDTO> groupMemberDTOS = new ArrayList<>();
        int currentRank = 1; // 현재 순위
        int previousDoneCount = -1; // 이전 멤버의 완료한 할 일 개수

        for (int i = 0; i < sortedMembers.size(); i++) {
            GroupMember groupMember = sortedMembers.get(i);
            Member member = groupMember.getMember(); // Member 정보 가져오기
            int doneCount = groupMember.getTodoCount(); // 완료한 할 일 개수 가져오기

            // 동일한 개수인 경우 순위를 동일하게 부여
            if (doneCount != previousDoneCount) {
                currentRank = i + 1; // 새로운 순위 설정
            }

            // GroupMemberDTO 생성
            groupMemberDTOS.add(GroupMemberDTO.builder()
                    .id(member.getId()) // Member ID
                    .isLeader(groupMember.getIsLeader()) // 리더 여부
                    .nickname(member.getNickname()) // 멤버 이름
                    .color(groupMember.getColor().getHexCode()) // 색상
                    .contributionRank(currentRank) // 순위 매기기
                    .build());

            previousDoneCount = doneCount; // 현재 완료한 할 일 개수를 이전 완료한 할 일 개수로 업데이트
        }

        this.members = groupMemberDTOS; // 결과를 members에 설정
    }


}
