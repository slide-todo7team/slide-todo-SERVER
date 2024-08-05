package com.slide_todo.slide_todoApp.domain.goal;

import com.slide_todo.slide_todoApp.domain.todo.GroupTodo;
import com.slide_todo.slide_todoApp.domain.todo.Todo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.el.parser.BooleanNode;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLRestriction("is_deleted = false") // 논리 삭제된 데이터는 조회하지 않도록 설정
public abstract class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @OneToMany(mappedBy = "goal", fetch = FetchType.LAZY)
    private List<Todo> todos = new ArrayList<>();

    private String title;

    private Integer progressRate;

    private Boolean isDeleted;

    public Goal(String title) {
        this.title = title;
        this.progressRate = 0;
        this.isDeleted = false;
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
}
