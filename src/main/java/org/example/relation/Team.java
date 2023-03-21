package org.example.relation;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Team {

    @Id
    @Column(name = "ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team") // 필드명 (not Physical Database Column ID)
    private List<Member> members  = new ArrayList<>();
    // 20230227 new ArrayList() 주석처리 되어있는거 다시 풀었음. 뭔가 다른 테스트에서 쓰고있었다면 롤백바람

    /**
     * 양방향 연관관계 편의 메서드
     */
    public void addMember(Member member){
        member.changeTeam(this);
    }

    public Team(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member pickOne(){
        return this.members.stream().findAny().get();
    }
}
