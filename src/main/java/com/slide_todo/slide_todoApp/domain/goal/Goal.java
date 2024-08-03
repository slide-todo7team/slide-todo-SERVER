package com.slide_todo.slide_todoApp.domain.goal;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.el.parser.BooleanNode;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Getter
public abstract class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    private String title;

    private Integer progressRate;

    private Boolean isDeleted;

}
