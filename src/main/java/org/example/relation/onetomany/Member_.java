package org.example.relation.onetomany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member_ {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /**
     * 이거 중요함!
     * 1:N 관계에서 1이 연관관계의 주인이라 객체상에서 결정했을 때,
     * 양방향을 하는 방법은 `야매`로 있다.
     * 여기서도 @JoinColumn을 쓰는것인데, 이는 그 전에 1쪽에 @JoinColumn으로 연관관계의 주인을 명시했던것과는 맥락이 다르다.
     * 여기서 @JoinColumn을 명시하더라도 N 쪽을 연관관계의 주인이라 명시하는것은 아니다.
     */
    @ManyToOne
    @JoinColumn(name= "TEAM_ID", insertable = false,updatable = false) // 살짝 mappedBy랑 비슷한거같음 "읽기전용"에 집중
    private Team_ team;
}