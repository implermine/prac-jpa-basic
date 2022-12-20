package org.example.customDomain.notFound;

import org.example.BaseCondition;
import org.example.customDomain.notFound.noForeignKey.Member_noFk;
import org.example.customDomain.notFound.noForeignKey.Team_noFk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * FK만 없을 때, 어떤식으로 운용되는지 확인
 */
public class NoFkTest extends BaseCondition {

    /**
     * 1.
     * 2.
     * 3.
     */
    @Test
    @DisplayName("FK 없을 때 어떤식으로 운용되는지 확인")
    void test(){
        //given

        /**
         * memberA, memberB는 Team1에 소속
         * memberC는 Team2에 소속
         *
         * Team1을 삭제
         */
        Member_noFk memberA = new Member_noFk(1L, "memberA");
        Member_noFk memberB = new Member_noFk(2L, "memberB");
        Member_noFk memberC = new Member_noFk(3L, "memberC");
        Team_noFk team1 = new Team_noFk(1L,"team1");
        Team_noFk team2 = new Team_noFk(2L,"team2");
        memberA.setTeam(team1);
        memberB.setTeam(team1);
        memberC.setTeam(team2);
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.flush();
        em.clear();


        // team1 삭제
        Team_noFk foundTeam = em.find(Team_noFk.class, 1L);
        em.remove(foundTeam);
        em.flush();
        em.clear();


        line("after scenario");

        // 이거 진짜 기묘하네
        /**
         * left outer join(default)일때나, inner join(nullable=false) 일 때나,
         * left outer join일 땐, right가 null이면 left도 null이라 해버리고,
         * inner join일 땐, right가 null이면 어차피 left도 null이니까 null 반환한다.
         *
         * join의 방향성과 관계없이, FK contraint가 없다면,
         * EAGER 기준
         * join 후에 양쪽 연관관계 (Member-Team)중에 하나라도 없다면, em.find()는 null을 반환한다.
         * ( Member 가 없을때 null인건 em.find()할때 당연하고, Team 이 없을 때 이걸 null이라 하는게 기묘함)
         *
         * LAZY 기준
         * join을 일단 안한다 em.find할 때,
         * 그리고, getTeam()같은걸로 proxy를 열어볼 때, (일단 fk는 아니지만, 그 연관관계의 키를 갖고 있으니 강제 Eager Loading이 발생하진 않는다.)
         * 그 key로 SELECT 쿼리를 team쪽에 날리고, 그 entity가 존재하지 않으면 그제서야 여기서 `EntityNotFoundException`이 발생한다.
         *
         *
         */
        Member_noFk foundMember = em.find(Member_noFk.class, 1L);
        line("after find");
        System.out.println(foundMember.getTeam().getName()); // exception occurs

    }
}
