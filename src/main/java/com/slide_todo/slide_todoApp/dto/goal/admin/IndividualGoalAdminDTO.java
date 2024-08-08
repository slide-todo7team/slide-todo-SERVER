package com.slide_todo.slide_todoApp.dto.goal.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slide_todo.slide_todoApp.domain.goal.IndividualGoal;
import com.slide_todo.slide_todoApp.domain.member.Member;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class IndividualGoalAdminDTO {

  @JsonProperty("total_count")
  private Long totalCount;
  @JsonProperty("current_page")
  private Long currentPage;
  @JsonProperty("goals")
  private List<IndividualGoalInListDTO> goals;

  public IndividualGoalAdminDTO(Long totalCount, Long currentPage, List<IndividualGoal> goals) {
    this.totalCount = totalCount;
    this.currentPage = currentPage;
    this.goals = goals.stream().map(IndividualGoalInListDTO::new).toList();
  }



  @Data
  public static class IndividualGoalInListDTO {

    @JsonProperty("member")
    private MemberInGoalDTO memberInGoalDTO;
    private Long id;
    private String title;
    @JsonProperty("progress_rate")
    private BigDecimal progressRate;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public IndividualGoalInListDTO(IndividualGoal individualGoal) {
      this.memberInGoalDTO = new MemberInGoalDTO(individualGoal.getMember());
      this.id = individualGoal.getId();
      this.title = individualGoal.getTitle();
      this.progressRate = individualGoal.getProgressRate();
      this.createdAt = individualGoal.getCreatedAt();
      this.updatedAt = individualGoal.getUpdatedAt();
    }
  }


  @Data
  public static class MemberInGoalDTO {

    private Long id;
    private String nickname;

    public MemberInGoalDTO(Member member) {
      this.id = member.getId();
      this.nickname = member.getNickname();
    }
  }
}
