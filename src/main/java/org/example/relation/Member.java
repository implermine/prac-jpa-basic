package org.example.relation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member {

    @Id
    @Column(name = "ID")
    private Long id;


    @ManyToOne
    // name에 이쪽 fk (실제)DB 컬럼명, referencedColumnName에 이쪽 fk가 바라보는 저쪽 (실제) DB 컬럼명
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "ID")
    private Team team;

    @Column(name = "USERNAME")
    private String username;

    /**
     * 김영한은 연관관계 편의 메서드명을 setXXX를 안쓴다고 함.
     */
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
