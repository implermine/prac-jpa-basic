package org.example.customDomain.notFound.noForeignKey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@NoArgsConstructor
public class Member_noFk_DTO {

    private Long id;
    private String name;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="TEAM_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Team_noFk_DTO team;

    @Getter(AccessLevel.NONE)
    private Long teamId;

    public Long obtainTeamId(){
        return this.teamId;
    }

    public Member_noFk_DTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member_noFk_DTO(Long id, String name, Long teamId) {
        this.id = id;
        this.name = name;
        this.teamId = teamId;
    }
}
