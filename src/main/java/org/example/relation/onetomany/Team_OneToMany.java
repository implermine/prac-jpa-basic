package org.example.relation.onetomany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Team_OneToMany {

    @Id
    @Column(name = "ID")
    private Long id;

    private String name;

    /**
     * 1:N에서 1 측이 연관관계의 주인이면 mappedBy 속성이 없고, JoinColumn을 사용한다.
     */
    @OneToMany
    @JoinColumn(name = "TEAM_ID") // OneToMany에서 JoinColumn 스펙은 ManyToOne일때와는 다르다, 이번엔 "저쪽" DB 컬럼명이다.
    // @JoinColumn이 존재하지 않는다면 JoinTable을 생성한다. 이는 이쪽 테이블에 relation에 대한 정보가 들어갈 공간이 없기 때문이다.
    // 따라서 이 경우엔 연관관계의 주인이 JoinTable이며, 더 이상 반대쪽인 Member에 Update를 쳐서 연관관계를 관리하는것이 아닌,
    // JoinTable에 INSERT를 하여 연관관계를 관리한다.
    private final List<Member_OneToMany> members = new ArrayList<>();

}