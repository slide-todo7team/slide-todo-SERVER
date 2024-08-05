package com.slide_todo.slide_todoApp.dto.group;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupCreateDTO {
    private String title;
    private String secretCode;
}
