package com.slide_todo.slide_todoApp.domain.goal;

import com.slide_todo.slide_todoApp.domain.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLRestriction;

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
    }

    protected IndividualGoal() {
        super();
    }
}
