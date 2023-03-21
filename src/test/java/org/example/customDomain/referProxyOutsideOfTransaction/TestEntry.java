package org.example.customDomain.referProxyOutsideOfTransaction;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.example.relation.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestEntry extends BaseCondition {

    @BeforeEach
    void scenario(){
        /**
         * 시나리오
         *
         * memberA, memberB는 Team1에 소속
         * memberC, memberD는 Team2에 소속
         * memberE는 아무팀에도 소속되지 않음.
         *
         * Team1을 삭제
         */

        Member memberA = new Member(1L, "memberA");
        Member memberB = new Member(2L, "memberB");
        Member memberC = new Member(3L, "memberC");
        Member memberD = new Member(4L, "memberD");
        Member memberE = new Member(5L, "memberE");
        Team team1 = new Team(1L,"team1");
        Team team2 = new Team(2L,"team2");
        memberA.changeTeam(team1);
        memberB.changeTeam(team1);
        memberC.changeTeam(team2);
        memberD.changeTeam(team2);
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);
        em.flush();
        em.clear();

        line("After Scenario");
    }

    @Test
    @DisplayName("영속성 밖에서 `이미` 불러온 프록시를 조회")
    void test(){
        var member = em.find(Member.class,1L);
        String name = member.getTeam().getName();
        line("name = " + name);

        tx.commit();

        line("after transaction close");

        System.out.println(member.getTeam().getName());
        System.out.println("hey");
    }
}
