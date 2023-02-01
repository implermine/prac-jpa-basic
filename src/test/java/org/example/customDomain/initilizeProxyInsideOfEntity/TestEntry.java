package org.example.customDomain.initilizeProxyInsideOfEntity;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.example.relation.Team;
import org.junit.jupiter.api.Test;

public class TestEntry extends BaseCondition {

    @Test
    void test(){

        Member member = new Member();
        member.setUsername("member1");
        member.setId(1L);


        Team team = new Team();
        team.setId(1L);
        team.setName("teamA");

        em.persist(team);
        member.setTeam(team);

        em.persist(member);

        em.flush();
        em.clear();

        Team foundTeam = em.find(Team.class, 1L);

        line("before pick");
        foundTeam.pickOne();
        line("after pick");

    }
}
