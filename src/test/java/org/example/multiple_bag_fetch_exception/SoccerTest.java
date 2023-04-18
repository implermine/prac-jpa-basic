package org.example.multiple_bag_fetch_exception;

import org.example.BaseCondition;
import org.hibernate.Hibernate;
import org.hibernate.jpa.QueryHints;
import org.hibernate.loader.MultipleBagFetchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class SoccerTest extends BaseCondition {

    @BeforeEach
    void scenario() {
        SoccerTeam team1 = new SoccerTeam(1L, "team1");
        SoccerTeam team2 = new SoccerTeam(2L, "team2");
        //SoccerTeam team3 = new SoccerTeam(3L, "team3");

        em.persist(team1);
        em.persist(team2);
        //em.persist(team3);


        SoccerMember memberA = new SoccerMember(1L, "memberA");
        SoccerMember memberB = new SoccerMember(2L, "memberB");
        SoccerMember memberC = new SoccerMember(3L, "memberC");
        SoccerMember memberD = new SoccerMember(4L, "memberD");
        SoccerMember memberE = new SoccerMember(5L, "memberE");
        //SoccerMember memberF = new SoccerMember(6L, "memberF");


        memberA.setSoccerTeam(team1);
        memberB.setSoccerTeam(team1);
        memberC.setSoccerTeam(team1);

        //memberF.setSoccerTeam(team3);

        memberD.setSoccerTeam(team2);
        memberE.setSoccerTeam(team2);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);
        em.persist(memberE);
        //em.persist(memberF);

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
    void test() {
        String jpql = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
                + "LEFT JOIN FETCH soccerTeam.soccerMembers "
                + "LEFT JOIN FETCH soccerTeam.soccerTeamSongs ";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        System.out.println("hey");

    }


    @Test
    void fetchByRelationOnly(){
        String jpql = "SELECT soccerTeam FROM SoccerTeam soccerTeam ";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .getResultList();

        for (SoccerTeam soccerTeam : soccerTeams) {
            //touch proxy or initialize
            List<SoccerMember> soccerMembers = soccerTeam.getSoccerMembers();
            Hibernate.initialize(soccerMembers);
            List<SoccerTeamSong> soccerTeamSongs = soccerTeam.getSoccerTeamSongs();
            Hibernate.initialize(soccerTeamSongs);
        }

        System.out.println("hey");
    }

    @Test
    @DisplayName("Bag order docs")
    void orderingDocs() {
        String jpql = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
                + "LEFT JOIN FETCH soccerTeam.soccerMembers soccerMember " +
                "ORDER BY soccerMember.name DESC";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        System.out.println("hey");
    }

    @Test
    void doc2() {
        String jpql = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
                + "JOIN FETCH soccerTeam.soccerMembers "
                + "JOIN FETCH soccerTeam.soccerTeamSongs ";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        SoccerTeam soccerTeam = soccerTeams.stream().findAny().get();

        System.out.println("song class : " + soccerTeam.getSoccerTeamSongs().getClass());
        System.out.println("member class : " + soccerTeam.getSoccerMembers().getClass());

    }

    @Test
    void doc3() {
        String findAllTeamQuery = "SELECT soccerTeam FROM SoccerTeam soccerTeam";

        List<SoccerTeam> soccerTeamList = em.createQuery(findAllTeamQuery, SoccerTeam.class)
                .getResultList();

        for (SoccerTeam soccerTeam : soccerTeamList) {
            System.out.println("teamName : " + soccerTeam.getName());

            System.out.println("  <member>");
            var soccerMembers = soccerTeam.getSoccerMembers();
            for (SoccerMember soccerMember : soccerMembers) {
                System.out.println("    memberName : " + soccerMember.getName());
            }

            System.out.println("  <teamSong>");
            List<SoccerTeamSong> soccerTeamSongs = soccerTeam.getSoccerTeamSongs();
            for (SoccerTeamSong soccerTeamSong : soccerTeamSongs) {
                System.out.println("    teamSongId : " + soccerTeamSong.getName());
            }
        }
    }

    @Test
    void doc4() {
        String jpql = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
                + "LEFT JOIN FETCH soccerTeam.soccerMembers ";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        String jpql2 = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
                + "LEFT JOIN FETCH soccerTeam.soccerTeamSongs "
                + "WHERE soccerTeam IN :soccerTeams";

        soccerTeams = em.createQuery(jpql2, SoccerTeam.class)
                .setParameter("soccerTeams", soccerTeams)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();
    }

    @Test
    void doc5() {
        String jpql = "SELECT DISTINCT soccerTeam " +
                      "FROM SoccerTeam soccerTeam " +
                      "JOIN FETCH soccerTeam.soccerMembers ";

        List<SoccerTeam> soccerTeams =
                em.createQuery(jpql, SoccerTeam.class)
                        .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                        .getResultList();
    }
    //@Test
    //void t(){
    //    String jpql = "SELECT DISTINCT soccerTeam FROM SoccerTeam soccerTeam "
    //            + "LEFT JOIN FETCH soccerTeam.soccerMembers "
    //            + "LEFT JOIN FETCH soccerTeam.soccerTeamSongs ";
    //
    //
    //    List<SoccerTeam> resultList = em.createQuery(jpql, SoccerTeam.class).getResultList();
    //
    //    for (SoccerTeam soccerTeam : resultList) {
    //
    //        System.out.println(soccerTeam.getName());
    //
    //        System.out.println(" <member>");
    //        Set<SoccerMember> soccerMembers = soccerTeam.getSoccerMembers();
    //        for (SoccerMember soccerMember : soccerMembers) {
    //            System.out.println("  " + soccerMember.getName());
    //        }
    //
    //        System.out.println(" <teamSong>");
    //        List<SoccerTeamSong> soccerTeamSongs = soccerTeam.getSoccerTeamSongs();
    //        for (SoccerTeamSong soccerTeamSong : soccerTeamSongs) {
    //            System.out.println("  " + soccerTeamSong.getId());
    //        }
    //    }
    //}

    @Test
    @DisplayName("default batch fetch size")
    void fetchSize() {
        String jpql = "SELECT soccerTeam FROM SoccerTeam soccerTeam ";

        List<SoccerTeam> soccerTeams = em.createQuery(jpql, SoccerTeam.class)
                .getResultList();


        //PersistentBag에 touch가 가해지면 (proxy만 touch해도)
        //그 PersistentBag의 BackReference인 SoccerTeamList의 후보 그룹 전부 fetch한다.

        //for (SoccerTeam soccerTeam : soccerTeams) {
        //    List<SoccerMember> soccerMembers = soccerTeam.getSoccerMembers();
        //    for (SoccerMember soccerMember : soccerMembers) {
        //        System.out.println(soccerMember.getName());
        //    }
        //}
        //
        //SoccerTeam soccerTeam = soccerTeams.get(2);
        //List<SoccerMember> soccerMembers = soccerTeam.getSoccerMembers();
        //for (SoccerMember soccerMember : soccerMembers) {
        //    System.out.println(soccerMember.getName());
        //}
        //
        //SoccerTeam soccerTeam1 = soccerTeams.get(1);
        //List<SoccerMember> soccerMembers1 = soccerTeam1.getSoccerMembers();
        //for (SoccerMember soccerMember : soccerMembers1) {
        //    System.out.println(soccerMember.getName());
        //}
        //
        //SoccerTeam soccerTeam2 = soccerTeams.get(0);
        //List<SoccerMember> soccerMembers2 = soccerTeam2.getSoccerMembers();
        //for (SoccerMember soccerMember : soccerMembers2) {
        //    System.out.println(soccerMember.getName());
        //}


    }

    @Test
    void fetch2() {
        String jpql = "SELECT soccerMember FROM SoccerMember soccerMember ";

        List<SoccerMember> soccerMembers = em.createQuery(jpql, SoccerMember.class)
                .getResultList();


        for (SoccerMember soccerMember : soccerMembers) {
            SoccerTeam soccerTeam = soccerMember.getSoccerTeam();
            System.out.println(soccerTeam.getName());
        }
    }

    @Test
    @DisplayName("커밋")
    void commit() {
        tx.commit();
    }


}
