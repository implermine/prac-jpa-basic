package org.example.customDomain.forcedEagerLoading;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Member_Force {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_SURROGATED_MEMBER_FK", referencedColumnName = "SURROGATED_MEMBER_FK")
    private Team_Force team;

    public Member_Force(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setTeam(Team_Force team) {
        this.team = team;
    }
}
