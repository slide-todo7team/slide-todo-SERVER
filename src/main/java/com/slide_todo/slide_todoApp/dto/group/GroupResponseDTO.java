package com.slide_todo.slide_todoApp.dto.group;
import com.slide_todo.slide_todoApp.domain.group.Group;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class GroupResponseDTO {
    private Long id;
    private String title;
    private String secretCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GroupResponseDTO(Group group) {
        this.id = group.getId();
        this.title = group.getTitle();
        this.secretCode = group.getSecretCode();
        this.createdAt = group.getCreatedAt();
        this.updatedAt = group.getUpdatedAt();
    }
}
