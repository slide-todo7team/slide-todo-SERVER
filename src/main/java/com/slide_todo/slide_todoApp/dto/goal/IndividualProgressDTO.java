package com.slide_todo.slide_todoApp.dto.goal;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class IndividualProgressDTO {
    private double progress;
}
