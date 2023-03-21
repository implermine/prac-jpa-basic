package org.example.customDomain.ContextMemoized;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.example.SomeMember;
import org.example.customDomain.notFound.Member_NotFound;
import org.example.customDomain.notFound.Team_NotFound;
import org.example.relation.Member;
import org.example.relation.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.List;

public class ContextMemoized extends BaseCondition {

    @Test
    @DisplayName("JPQL 날리기 전에 flush") // -> 참
    void should_fire_sql_before_JPQL(){

        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("다람쥐");

        em.persist(someMember);

        System.out.println(">>> before JPQL");

        List<SomeMember> members = em.createQuery("select m from SomeMember m", SomeMember.class).getResultList();

        System.out.println(">>> after JPQL");

    }

    @Test
    @DisplayName("JPQL 조회 쿼리는 영속화를 수행하는가?") // -> 참
    void should_JPQL_persist_entity(){

        /**
         * given
         *
         * 미리 SomeMember DB에 저장하고, em 초기화
         */
        SomeMember someMember = new SomeMember();
        someMember.setName("cat");
        someMember.setId(1L);

        em.persist(someMember);

        em.flush();
        em.clear();

        /**
         * when
         *
         * JPQL로 조회 쿼리를 fire 하였을 때,
         */
        SomeMember foundMember = (SomeMember)(em.createQuery("select m from SomeMember m where m.id = 1").getSingleResult());

        Assertions.assertThat(em.contains(foundMember)).isTrue();

    }

    @Test
    @DisplayName("영속성 컨텍스트에 WHERE CLAUSE에 의한 특정 연관관계를 호출해서 채워두면, 계속 그 조건이 유지되는가?") // 네
    void test_context_memoized(){

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

        TypedQuery<Team> query = em.createQuery("SELECT t FROM Team t JOIN FETCH t.members m WHERE m.id = :memberId", Team.class);
        query.setParameter("memberId" , 1L);
        List<Team> resultList = query.getResultList();

        System.out.println("hey");
        line("start experiment");

        // result2는 원래라면, team1에 memberA,memberB를 바인딩해서 가져와야 하지만,
        // 이미 team1엔 memberA `만` 바인딩 되어있다. 이때 resultList[0]과 resultList2[0]이 서로 같은 주소의 Member PersistentBag을 들고 있는데,
        // 이에대해 PersistentBag은 Id (이 컨텍스트에선 TeamId)를 가진 컬렉션이라 유추 할 수 있다.
        List<Team> resultList2 = em.createQuery("SELECT t FROM Team t JOIN FETCH t.members m", Team.class).getResultList();

        System.out.println("hey2");

    }


}
