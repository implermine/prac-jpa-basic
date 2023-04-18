package org.example.multiple_bag_fetch_exception;

import org.example.BaseCondition;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

/**
 * index
 */
public class BaseBallTest extends BaseCondition {

    @BeforeEach
    void scenario() {
        BaseBallTeam team1 = new BaseBallTeam(1L, "team1");
        BaseBallTeam team2 = new BaseBallTeam(2L, "team2");

        em.persist(team1);
        em.persist(team2);


        BaseBallMember memberA = new BaseBallMember(1L, "memberA", 0);
        BaseBallMember memberB = new BaseBallMember(2L, "memberB", 1);
        BaseBallMember memberC = new BaseBallMember(3L, "memberC", 2);
        BaseBallMember memberD = new BaseBallMember(4L, "memberD", 0);
        BaseBallMember memberE = new BaseBallMember(5L, "memberE", 1);

        memberA.setBaseBallTeam(team1);
        memberB.setBaseBallTeam(team1);
        memberC.setBaseBallTeam(team1);

        memberD.setBaseBallTeam(team2);
        memberE.setBaseBallTeam(team2);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);

        BaseBallTeamSong baseBallTeamSong101 = new BaseBallTeamSong(101L, "song101");
        BaseBallTeamSong baseBallTeamSong102 = new BaseBallTeamSong(102L, "song102");
        BaseBallTeamSong baseBallTeamSong103 = new BaseBallTeamSong(103L, "song103");

        BaseBallTeamSong baseBallTeamSong104 = new BaseBallTeamSong(104L, "song104");
        BaseBallTeamSong baseBallTeamSong105 = new BaseBallTeamSong(105L, "song105");

        baseBallTeamSong101.setBaseBallTeam(team2);
        baseBallTeamSong102.setBaseBallTeam(team2);
        baseBallTeamSong103.setBaseBallTeam(team2);
        baseBallTeamSong104.setBaseBallTeam(team1);
        baseBallTeamSong105.setBaseBallTeam(team1);

        em.persist(baseBallTeamSong101);
        em.persist(baseBallTeamSong102);
        em.persist(baseBallTeamSong103);
        em.persist(baseBallTeamSong104);
        em.persist(baseBallTeamSong105);

        em.flush();
        em.clear();

        line("after scenario");
    }
    @Test
    @DisplayName("Using a List in Single JPQL Query")
    void test() {
        String jpql = "SELECT DISTINCT baseBallTeam FROM BaseBallTeam baseBallTeam "
                + "JOIN FETCH baseBallTeam.baseBallMembers "
                + "JOIN FETCH baseBallTeam.baseBallTeamSongs ";

        List<BaseBallTeam> resultList = em.createQuery(jpql, BaseBallTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        System.out.println("hey");
        tx.commit();
    }


    @Test
    void doc1() {
        String jpql = "SELECT DISTINCT baseBallTeam FROM BaseBallTeam baseBallTeam "
                + "LEFT JOIN FETCH baseBallTeam.baseBallMembers "
                + "LEFT JOIN FETCH baseBallTeam.baseBallTeamSongs ";

        List<BaseBallTeam> resultList = em.createQuery(jpql, BaseBallTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        for (BaseBallTeam baseBallTeam : resultList) {

            System.out.println(baseBallTeam.getName());

            System.out.println(" <member>");
            List<BaseBallMember> baseBallMembers = baseBallTeam.getBaseBallMembers();
            for (BaseBallMember baseBallMember : baseBallMembers) {
                System.out.println("  " + baseBallMember.getName() + "/ arrangement_index : " + baseBallMember.getArrangementIndex());
            }

            System.out.println(" <teamSong>");
            List<BaseBallTeamSong> baseBallTeamSongs = baseBallTeam.getBaseBallTeamSongs();
            for (BaseBallTeamSong soccerTeamSong : baseBallTeamSongs) {
                System.out.println("  " + soccerTeamSong.getId());
            }
        }
    }

    @Test
    void doc2() {
        String jpql = "SELECT DISTINCT baseBallTeam FROM BaseBallTeam baseBallTeam "
                + "LEFT JOIN FETCH baseBallTeam.baseBallMembers "
                + "LEFT JOIN FETCH baseBallTeam.baseBallTeamSongs ";

        List<BaseBallTeam> resultList = em.createQuery(jpql, BaseBallTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        BaseBallTeam baseBallTeam = resultList.stream().findAny().get();

        System.out.println("song class : " + baseBallTeam.getBaseBallTeamSongs().getClass());
        System.out.println("member class : " + baseBallTeam.getBaseBallMembers().getClass());
    }

    @Test
    void commit() {
        tx.commit();
    }
}
