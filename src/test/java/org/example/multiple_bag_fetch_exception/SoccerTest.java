package org.example.multiple_bag_fetch_exception;

import org.example.BaseCondition;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SoccerTest extends BaseCondition {

    @BeforeEach
    void scenario(){
        SoccerTeam team1 = new SoccerTeam(1L, "team1");
        SoccerTeam team2 = new SoccerTeam(2L, "team2");

        em.persist(team1);
        em.persist(team2);


        SoccerMember memberA = new SoccerMember(1L ,"memberA");
        SoccerMember memberB = new SoccerMember(2L, "memberB");
        SoccerMember memberC = new SoccerMember(3L, "memberC");
        SoccerMember memberD = new SoccerMember(4L, "memberD");
        SoccerMember memberE = new SoccerMember(5L, "memberE");

        memberA.setSoccerTeam(team1);
        memberB.setSoccerTeam(team1);
        memberC.setSoccerTeam(team1);

        memberD.setSoccerTeam(team2);
        memberE.setSoccerTeam(team2);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);

        SoccerTeamSong song101 = new SoccerTeamSong(101L, "song101");
        SoccerTeamSong song102 = new SoccerTeamSong(102L, "song102");
        SoccerTeamSong song103 = new SoccerTeamSong(103L, "song103");

        SoccerTeamSong song104 = new SoccerTeamSong(104L, "song104");
        SoccerTeamSong song105 = new SoccerTeamSong(105L, "song105");

        song101.setSoccerTeam(team2);
        song102.setSoccerTeam(team2);
        song103.setSoccerTeam(team2);
        song104.setSoccerTeam(team1);
        song105.setSoccerTeam(team1);

        em.persist(song101);
        em.persist(song102);
        em.persist(song103);
        em.persist(song104);
        em.persist(song105);

        em.flush();
        em.clear();

        line("after scenario");
    }

    @Test
    @DisplayName("Using a List in Single JPQL Query")
    void test(){
        String jpql = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
                + "LEFT JOIN FETCH soccerTeam.soccerMembers "
                + "LEFT JOIN FETCH soccerTeam.soccerTeamSongs ";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        System.out.println("hey");

    }

    @Test
    @DisplayName("문서작성용")
    void docs(){
        String jpql = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
                + "LEFT JOIN FETCH soccerTeam.soccerMembers ";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .getResultList();

        for (SoccerTeam soccerTeam : soccerTeams) {
            System.out.println(soccerTeam.getName());
            for(SoccerMember soccerMember: soccerTeam.getSoccerMembers()){
                System.out.println("    "+ soccerMember.getName());
            }
        }

        System.out.println("stop");

    }

    @Test
    @DisplayName("커맛")
    void commit(){
        tx.commit();
    }
}
