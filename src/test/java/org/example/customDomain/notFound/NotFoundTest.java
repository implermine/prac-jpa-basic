package org.example.customDomain.notFound;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class NotFoundTest extends BaseCondition {

    @Test
    @DisplayName("Not Found exception trigger")
    void test(){
        // given
        /**
         * <시나리오>
         *
         * MemberA에 Team1에 대한 FK를 걸어두고(TEAM_ID = 1)
         *
         * Team1을 삭제하면
         *
         * MemberA는 broken foreign key constraint를 가지게 된다.
         *
         * 이때, NotFound는 어떻게 동작할 것인지
         */



        em.createNativeQuery("INSERT INTO MEMBER_NOTFOUND VALUES (:id,:name,:teamId)")
                .setParameter("id",1L)
                .setParameter("name","memberA")
                .setParameter("teamId", 1L)
                .executeUpdate();

        em.createNativeQuery("INSERT INTO TEAM_NOTFOUND VALUES (:id,:name)")
                .setParameter("id",1L)
                .setParameter("name","teamA")
                .executeUpdate();


        em.createNativeQuery("DELETE FROM TEAM_NOTFOUND WHERE ID = :id")
                .setParameter("id",1L)
                .executeUpdate();


//        Member_NotFound memberA = new Member_NotFound(1L, "memberA");
//        Team_NotFound team1 = new Team_NotFound(1L,"team1");
//        memberA.changeTeam(team1);
//        em.persist(team1);
//        em.persist(memberA);
//        em.flush();
//        em.clear();
//
//        line("member 와 team insert");
//
//        Member_NotFound foundMember = em.find(Member_NotFound.class, 1L); // 여기서 우선 LAZY 설정되어있는데 NotFound때매 EAGER하게 쿼리 나가는것 확인
//        line("member find");
//
//        em.remove(foundMember.getTeam());
//        line("team delete");
//
//        em.flush();
//        em.clear();
//        line("found member에서 Team delete");


        // when
        /**
         * NotFound가 어노테이션이 명시되어있으면 Eager로딩도 수행하며, Member는 존재하고 Team은 존재하지 않았을 경우, `그냥 Member도 null` 하다고 판단해버린다.
         * 다만, FK만 존재하지 않는 경우를 DB에 환경조성 해 두면, EntityNotFound Exception이 발생하고, 이는 Lazy 로딩도 정상적으로 수행된다.
         */
        Member_NotFound foundMember2 = null;
        try{
//            foundMember2 = em.createQuery("SELECT m FROM Member_NotFound m left outer join fetch m.team WHERE m.id = :id",Member_NotFound.class).setParameter("id",1L).getSingleResult();
            // 이건 또 null 그냥 EXCEPTION 모드일때도 설정 걍 해주네
            foundMember2 = em.find(Member_NotFound.class,1L);

        }catch (Exception e){
            e.printStackTrace();
        }


//        Team_NotFound team = foundMember2.getTeam();
//        line("get team");
//
//        // then
//        Assertions.assertThat(team).isNull();
    }

//    @Test
//    @DisplayName("Not Found Trigger when calling Many Side from One Side") // 이것도 안나는데?
//    void test2(){
//        // given
//        Team_NotFound team1 = new Team_NotFound(1L, "team1");
//        em.persist(team1);
//        em.flush();
//        em.clear();
//
//        // when
//        Team_NotFound foundTeam = em.find(Team_NotFound.class, 1L);
//        // then
//        List<Member_NotFound> members = foundTeam.getMembers();
//        for (Member_NotFound member : members) {
//            member.getName();
//        }
//
//        Assertions.assertThat(members).isEmpty();
//
//    }

    @Test
    @DisplayName("Not Found 없이 살아보기 with List")
    void test3(){

        //given

        // scenario
        /**
         * memberA, memberB는 Team1에 소속
         * memberC는 Team2에 소속
         *
         * Team1을 삭제하고
         *
         * 모든 member를 조회하면, memberA와 memberB의 경우 team을 referencing 할 때, EntityNotFoundException이 발생한다.
         */
        Member_NotFound memberA = new Member_NotFound(1L, "memberA");
        Member_NotFound memberB = new Member_NotFound(2L, "memberB");
        Member_NotFound memberC = new Member_NotFound(3L, "memberC");
        Team_NotFound team1 = new Team_NotFound(1L,"team1");
        Team_NotFound team2 = new Team_NotFound(2L,"team2");
        memberA.changeTeam(team1);
        memberB.changeTeam(team1);
        memberC.changeTeam(team2);
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.flush();
        em.clear();


        // team1 삭제
        Team_NotFound foundTeam = em.find(Team_NotFound.class, 1L);
        em.remove(foundTeam);
        em.flush();
        em.clear();


        // when
        List<Member_NotFound> memberList = em.createQuery("SELECT m FROM Member_NotFound as m", Member_NotFound.class).getResultList();

        for (Member_NotFound eachMember : memberList) {
            System.out.println(eachMember.getName()); // 여기까진 LazyLoading 안됌.
            try{
                System.out.println(eachMember.getTeam().getName()); // 여기서 발생하므로 try catch를 걸어서
            }catch (EntityNotFoundException exception){
                eachMember.setTeamNull();
            }
        }
        System.out.println(memberList.get(2).getTeam().getName());

        System.out.println("여기서 디버그 모드로 entity에 null 잘 세팅되었는지 확인");
    }

    @Test
    @DisplayName("Not Found 없이 살아보기 with List2")
    void test4(){

        //given

        // scenario
        /**
         * memberA, memberB는 Team1에 소속
         * memberC는 Team2에 소속
         *
         * Team1을 삭제하고
         *
         * 모든 member를 조회하면, memberA와 memberB의 경우 team을 referencing 할 때, EntityNotFoundException이 발생한다.
         */
        Member_NotFound memberA = new Member_NotFound(1L, "memberA");
        Member_NotFound memberB = new Member_NotFound(2L, "memberB");
        Member_NotFound memberC = new Member_NotFound(3L, "memberC");
        Team_NotFound team1 = new Team_NotFound(1L,"team1");
        Team_NotFound team2 = new Team_NotFound(2L,"team2");
        memberA.changeTeam(team1);
        memberB.changeTeam(team1);
        memberC.changeTeam(team2);
        em.persist(team1);
        em.persist(team2);
        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.flush();
        em.clear();


        // team1 삭제
        Team_NotFound foundTeam = em.find(Team_NotFound.class, 1L);
        em.remove(foundTeam);
        em.flush();
        em.clear();


        // when
        List<Member_NotFound> memberList = em.createQuery("SELECT m FROM Member_NotFound m join fetch m.team ", Member_NotFound.class).getResultList();

        // 애초에 하나만 가져오네 이러면 @NotFound 처럼.
        for (Member_NotFound eachMember : memberList) {
            System.out.println(eachMember.getName()); // 여기까진 LazyLoading 안됌.
            System.out.println(eachMember.getTeam().getName()); // 여기서 발생하므로 try catch를 걸어서
        }

        System.out.println("여기서 디버그 모드로 entity에 null 잘 세팅되었는지 확인");
    }
}
