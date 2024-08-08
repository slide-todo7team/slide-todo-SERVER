package com.slide_todo.slide_todoApp.domain.goal;

import com.slide_todo.slide_todoApp.domain.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("I")
public class IndividualGoal extends Goal {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public IndividualGoal(String title, Member member) {
        super(title);
        this.member = member;
        this.member.getIndividualGoals().add(this);
    }

    protected IndividualGoal() {
        super();
    }

    public void deleteIndividualGoal() {
        this.member.getIndividualGoals().remove(this);
        this.deleteGoal();
    }
}
