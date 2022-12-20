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
@Table(name  = "MEMBER")
public class Member {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    // Eager Loading Default가 left outer join이구나
    // nullable=false 해 두면 inner join이다.
    @ManyToOne(fetch = FetchType.EAGER)
    // name에 이쪽 fk (실제)DB 컬럼명, referencedColumnName에 이쪽 fk가 바라보는 저쪽 (실제) DB 컬럼명
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "ID",nullable = false)
    private Team team;



    /**
     * 김영한은 연관관계 편의 메서드명을 setXXX를 안쓴다고 함.
     */
    public void changeTeam(Team team) {

        // 기존 팀과 관계를 제거
        if(this.team != null){
            this.team.getMembers().remove(this);
        }


        this.team = team;
        team.getMembers().add(this);
    }

    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Member(Long id, String username, Team team) {
        this.id = id;
        this.username = username;
        this.team = team;
    }
}
