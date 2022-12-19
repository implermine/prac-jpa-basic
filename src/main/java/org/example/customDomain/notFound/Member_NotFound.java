package org.example.customDomain.notFound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class Member_NotFound {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name ="TEAM_ID")
//    @NotFound(action = NotFoundAction.EXCEPTION)
    @JoinColumn(name = "TEAM_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Team_NotFound team;

    public Member_NotFound(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void changeTeam(Team_NotFound team){
        if(this.team != null){
            this.team.getMembers().remove(this);

        }

        team.getMembers().add(this);
        this.team = team;
    }

    public void setTeamNull(){
        this.team = null;
    }

}
