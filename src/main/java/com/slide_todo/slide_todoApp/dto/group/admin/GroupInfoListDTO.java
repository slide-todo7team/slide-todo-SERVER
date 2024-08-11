package com.slide_todo.slide_todoApp.dto.group.admin;
import com.slide_todo.slide_todoApp.domain.goal.GroupGoal;
import com.slide_todo.slide_todoApp.domain.group.GroupMember;
import com.slide_todo.slide_todoApp.dto.group.GroupInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GroupInfoListDTO {
    private Long id;
    private String title;
    private String secretCode;
    private String createUser;
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

    @Data
    public static class GroupMemberDTO{
        private Long id;
        private String nickname;
        private String color;
        private Boolean isLeader;

        public GroupMemberDTO(GroupMember groupMember) {
            this.id = groupMember.getMember().getId();
            this.nickname = groupMember.getMember().getNickname();
            this.color = groupMember.getColor().getHexCode();
            this.isLeader = groupMember.getIsLeader();
        }
    }


}
