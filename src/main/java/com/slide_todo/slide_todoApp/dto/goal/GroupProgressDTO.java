package com.slide_todo.slide_todoApp.dto.goal;

import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupProgressDTO {
    private Integer completedPercent;
    private List<GroupGoalMemDTO> memebers;

    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupGoalMemDTO {
        private String name;
        private String color;
        private Integer contributionPercent;
    }

}

