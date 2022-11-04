package org.example.relation;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 양방향 매핑시 가장 많이 하는 실수들
 */
public class BidirectionalTest extends BaseCondition {

    /**
     * 원래 Database를 고려해보면, Foreign Key를 관리하는 Member쪽에만 데이터를 넣어주면 끝이지만,
     * 객체 관계를 고려하면 양쪽에 넣어주는게 맞다.
     */
    @Test
    @DisplayName("연관관계의 주인에 값을 입력해야 한다. (순수한 객체 관계를 고려하면 항상 양쪽 다 값을 입력해야 한다.)")
    void test1() {

        /**
         * Scenario
         *
         * 1. Team1 생성
         * 2. Member1 생성(not set Team as Team1)
         *
         * 3. Team1의 Members에 Member1이 들어가도록 등록
         *
         * 4. Team1 persist and flush
         *
         * 5. Clear
         *
         * 6. Member 조회 -> Member 본인이 Team에 등록되어 있는가? 논리적으론 들어가있어야된다, 왜냐면 Team1에서 Member1를 등록했기 때문에,
         * 그러나 연관관계의 주인이 아닌 객체에 값을 집어넣는건 `의미가 없다`.
         *
         */

        // 저장
        Team team = new Team();
        team.setId(1L);
        team.setName("TeamA");


        Member member = new Member();
        member.setId(1L);
        member.setUsername("member1");

        // 역방향(주인이 아닌 방향)만 연관관계 설정 (외래키 null)
        team.getMembers().add(member); // 어차피 읽기 전용임 따라서 여긴 의미없음
        // 그러나 읽기전용을 읽기전용처럼 쓰지 않으려면 고민을 해봐야한다.(양방향 객체지양 지원 메소드)


        em.persist(member);
        em.persist(team);
        em.flush();
        em.clear();

        Member foundMember1 = em.find(Member.class, 1L);

        System.out.println(lineDivider);
        Assertions.assertThat(foundMember1.getTeam()).isNull();
    }
}
