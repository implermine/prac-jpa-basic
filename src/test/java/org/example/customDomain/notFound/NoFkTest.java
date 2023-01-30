package org.example.customDomain.notFound;

import org.example.BaseCondition;
import org.example.customDomain.notFound.noForeignKey.Member_noFk;
import org.example.customDomain.notFound.noForeignKey.Team_noFk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * FK만 없을 때, 어떤식으로 운용되는지 확인
 */
public class NoFkTest extends BaseCondition {

    @BeforeEach
    void scenario(){
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

    }


    // NoFk / Team is Deleted
    // findById(em.find) [단건 조회] / EAGER or LAZY(설정해야함)
    @Test
    @DisplayName("FK 없을 때 어떤식으로 운용되는지 확인(기본)")
    void test(){
        // 이거 진짜 기묘하네
        /**
         * left outer join(Eager default)일때나, inner join(Eager, nullable=false) 일 때나,
         * left outer join일 땐, right가 null이면 left도 null이라 해버리고, -> what?
         * inner join일 땐, right가 null이면 어차피 left도 null이니까 null 반환한다.
         *
         * join의 방향성과 관계없이, FK contraint가 없다면,
         * EAGER 기준
         * join 후에 양쪽 연관관계 (Member-Team)중에 하나라도 없다면, em.find()는 null을 반환한다.
         * ( Member 가 없을때 null인건 em.find()할때 당연하고, Team만 없을 때 이걸 null이라 하는게 기묘함)
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
        System.out.println(foundMember.getTeam().getClass()); // null하진 않다 exception이 날 뿐 (LAZY)
        System.out.println(foundMember.getTeam().getId());
//        System.out.println(foundMember.getTeam().getName()); // exception occurs
    }

    // NoFk / Team is Deleted
    // findAll(em.createQuerty(jpql)) [다건 조회] / EAGER
    @Test
    @DisplayName("FK 없을 때 어떤식으로 운용되는지 확인 ( jpql로 리스트 )")
    void test2(){
        List<Member_noFk> memberList = em.createQuery("SELECT m FROM Member_noFk m", Member_noFk.class).getResultList();
        System.out.println("쿼리 확인");
        
        // 여기서 Member는 A,B,C가 존재한다.
        // LAZY일때는 memberA와 memberB (team1에 연결되어있는) 의 team을 조회하려 한다면, EntityNotFoundException이 발생 할 것이라
        // 예상할 수 있다.

        System.out.println("stream 치기 전");
        Member_noFk memberA = memberList.stream().filter(member -> member.getName().equals("memberA")).findAny().get();
        System.out.println("stream 친 후");


        System.out.println("memberA.getTeam().getName() 전");
        System.out.println(memberA.getTeam().getName());
        // EntityNotFoundException: Unable to find org.example.customDomain.notFound.noForeignKey.Team_noFk with id 1

        // Member_noFk 엔티티의 team 조회 조건을 EAGER로 설정하면 exception이 발생 치 않고, memberList의 size가 0일 것 같은데
        // 그것도 LAZY -> EAGER 바꿔서 확인

        // answer : 이건 위의 test1() 처럼 그냥 null값을 가져오게 되진 않는다.
        // JPQL로 memberList를 가져오게되면, 강제 로딩을 수행하며(N+1) EntityNotFoundException이 동일하게 발생한다.

        // evaluation : 그럼 findById(em.find)를 사용할때 다음과 같이 정리할 수 있다.


        // NoFk / Team is Deleted

        // findById(em.find) [단건 조회] / EAGER
        /**
         * left outer join과 inner join 모두 null 값을 가져오며
         *
         * 의도 된 (자연스러운) 동작이다.
         */
        // findById(em.find) [단건 조회] / LAZY
        /**
         * join없이 쿼리가 나가며
         *
         * lazy-loading을 위해 쿼리가 나가면서 EntityNotFoundException이 발생한다.
         *
         * 이 점에 대해서는 나도 어느정도 공감한다, @NotFound도 그냥 이렇게, Runtime Exception을 내면 안되나? 왜 굳이 EAGER하려는거지?
         *
         */


        // findAll(em.createQuerty(jpql)) [다건 조회] / EAGER
        /**
         * join 쿼리가 발생하진 않는다. (JPQL로 그냥 JOIN없이 쿼리 친거라서)
         *
         * 그렇지만 EAGER에 의해서, N+1 쿼리를 발생시킨다.
         *
         * 이때 EntityNotFoundException이 발생한다.
         */

        // findAll(em.createQuerty(jpql)) [다건 조회] / LAZY
        /**
         * join 쿼리는 당연히 발생하지 않으며
         * LAZY하게 쿼리를 발생시키며 EntityNotFoundException을 낼 수 있다.
         */
    }

    @Test
    @DisplayName("FK 없을 때 어떤식으로 운용되는지 확인 ( jpql로 left outer join 리스트 )")
    void test3() {
        List<Member_noFk> memberList = em.createQuery("SELECT m FROM Member_noFk m left join fetch m.team t", Member_noFk.class).getResultList();

        System.out.println("member iter");
        for (Member_noFk eachMember : memberList) {
            System.out.println(eachMember.getName());
        }

        System.out.println("member team iter");
        for (Member_noFk eachMember : memberList) {
            System.out.println(eachMember.getTeam().getName());
        }

        /**
         * 우선 결론은,
         *
         * EAGER의 경우
         * left outer join 나간 후에, memberA의 fk인 1로 team where team.id = 1로 추가쿼리 (즉 2개)
         * 나가고 EntityNotFoundException
         *
         * LAZY의 경우
         * left outer join 나간 후에
         * member iter 다 돌고
         * team iter 돌려고 하다가 proxy 까면서 EntityNotFoundException 발생함.
         *
         * 내가 예상컨데,
         * 둘다 프록싱을 시도하고, EAGER는 쿼리 이후에, 채워지지 못한 memberA의 team proxy에 대해서 `도` 채워주려고 (못 채웠음)
         * 추가 쿼리를 발생시키다가 -> EntityNotFoundException
         *
         * LAZY는 쿼리 이후에, 뭐 알다시피 그냥 proxy 되어있는걸 까다가 EntityNotFoundException 발생
         *
         */
    }


}
