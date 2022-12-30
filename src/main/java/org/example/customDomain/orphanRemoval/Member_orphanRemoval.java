package org.example.customDomain.orphanRemoval;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member_orphanRemoval {

    @Id
    private Long id;

    @EqualsAndHashCode.Include
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team_orphanRemoval team;


    public Member_orphanRemoval(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member_orphanRemoval(Long id, String name, Team_orphanRemoval team) {
        this.id = id;
        this.name = name;
        this.team = team;
    }
}
