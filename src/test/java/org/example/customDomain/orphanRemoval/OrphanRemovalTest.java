package org.example.customDomain.orphanRemoval;

import org.example.BaseCondition;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrphanRemovalTest extends BaseCondition {

    @BeforeEach
    void scenario(){

        Team_orphanRemoval team1 = new Team_orphanRemoval(1L, "team1");


        Member_orphanRemoval memberA = new Member_orphanRemoval(1L, "memberA", team1);
        Member_orphanRemoval memberB = new Member_orphanRemoval(2L, "memberB", team1);
        Member_orphanRemoval memberC = new Member_orphanRemoval(3L, "memberC", team1);
        Member_orphanRemoval memberD = new Member_orphanRemoval(4L, "memberD", team1);

        em.persist(team1);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

        em.flush();
        em.clear();


        line("After Scenario");
    }


    @Test
    @DisplayName("orphanRemoval=true의 기능 동작 확인")
    void test(){
        Team_orphanRemoval foundTeam = em.find(Team_orphanRemoval.class, 1L);

        foundTeam.removeFirst(new Member_orphanRemoval(1L,"memberA"));

        em.flush();
    }
}
