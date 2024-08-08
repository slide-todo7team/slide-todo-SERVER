package com.slide_todo.slide_todoApp.dto.goal;

import java.math.BigDecimal;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupProgressDTO {
    private BigDecimal completedPercent;
    private List<GroupGoalMemDTO> memebers;

    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupGoalMemDTO {
        private String nickname;
        private String color;
        private Integer contributionPercent;
    }

}

