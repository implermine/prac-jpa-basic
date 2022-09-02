package org.example.proxy;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.example.relation.Team;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FetchTypeTest extends BaseCondition {

    @Test
    @DisplayName("Lazy Loading")
    void lazy(){

        // given
        Team team = new Team();
        team.setId(1L);
        team.setName("teamA");
        Member member = new Member();
        member.setId(1L);
        member.setUsername("memberA");
        member.setTeam(team);

        // 영속화 먼저 시켜야함 ref: @DisplayName(영속화 되어있지 않은 Foreign Entity는 UPDATE를 날림)
        em.persist(team);
        em.persist(member);
        em.flush();
        em.clear();

        // when
        Team findTeam = em.find(Team.class, 1L);

        // then
        List<Member> members = findTeam.getMembers();

        System.out.println("\n\n === Is Proxy?=== \n");
        System.out.println(members.getClass()); // <- org.hibernate.collection.internal.PersistentBag (Proxy)

        // initialize
        Hibernate.initialize(members);
    }
}