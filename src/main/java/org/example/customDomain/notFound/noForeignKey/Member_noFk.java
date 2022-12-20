package org.example.customDomain.notFound.noForeignKey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member_noFk {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="TEAM_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Team_noFk team;

    public Member_noFk(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
