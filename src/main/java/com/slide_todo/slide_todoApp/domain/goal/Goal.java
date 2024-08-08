package com.slide_todo.slide_todoApp.domain.goal;

import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.el.parser.BooleanNode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@SQLRestriction("is_deleted = false")
public abstract class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY)
    private List<Todo> todos = new ArrayList<>();

    private String title;

    @Column(precision = 4, scale = 1)
    private BigDecimal progressRate;

    private Boolean isDeleted;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Goal(String title) {
        this.title = title;
        this.progressRate = BigDecimal.valueOf(0);
        this.isDeleted = false;
    }

    @Transient
    public String getDtype() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    /**
     * 목표의 할 일을 모두 논리 삭제
     */
    public void deleteGoal() {
        this.isDeleted = true;
        for (Todo todo : todos) {
            todo.deleteTodo();
        }
    }

    /**
     * 목표 진행률 계산 메소드 필요
     */
    public void updateProgressRate() {
        long total = todos.stream().filter(todo -> !todo.getIsDeleted()).count();
        long done = todos.stream().filter(Todo::getIsDone)
            .filter(todo -> !todo.getIsDeleted()).count();
        this.progressRate = BigDecimal.valueOf(total > 0 ? (double) done / total * 100 : 0);
    }
}
