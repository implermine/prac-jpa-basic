package org.example.relation;

import org.example.BaseCondition;
import org.example.relation.onetomany.Member_OneToMany;
import org.example.relation.onetomany.Team_OneToMany;
import org.junit.jupiter.api.*;

import java.util.List;

/**
 * 1. 테이블의 일대다 관계는 항상 `다(N) 쪽에 외래키가 있음`
 * 따라서, 객체와 테이블의 차이 때문에 반대편 테이블의 외래 키를 관리하는 특이한 구조
 * @JoinColumn을 꼭 사용해야 함. 그렇지 않으면 조인 테이블 방식을 사용함(중간에 테이블을 하나 추가함)
 */
public class OneToManyTest extends BaseCondition {

    /**
     * 일대다 단방향 매핑의 단점
     * 1. 엔티티가 관리하는 외래 키가 다른 테이블에 있음
     * 2. 연관관계 관리를 위해 추가로 UPDATE SQL 실행
     */
    @Test
    @DisplayName("1:N 관계에서 1이 연관관계의 주인인 경우")
    void one_to_many_insert(){
        Member_OneToMany member = new Member_OneToMany();
        member.setId(1L);
        member.setUsername("member1");
        em.persist(member);

        /**
         * 원래같앴으면 Team을 먼저 만들고 member를 그 다음 만든다음에,
         * member.setTeam(team) 해 줬을 테지만,
         * 이번엔 연관관계의 주인이 반대이니,
         * member 먼저 만들고 team을 setting 중.
         */
        Team_OneToMany team_ = new Team_OneToMany();
        team_.setId(1L);
        team_.setName("team1");

        // 여기가 좀 애매해. 이게 되나? UPDATE 쿼리로 되네요.
        team_.getMembers().add(member);
        em.persist(team_);

        System.out.println("\n ===================================== Before Flush ===================================== \n");
        System.out.println("===================================== Expect 2 Insert Query And 1 Update Query =====================================");
        // 추가로 UPDATE SQL 실행이란 단점이 여기서 드러남.
        em.flush();
        System.out.println("\n ===================================== After Flush ===================================== \n");

        tx.commit();
    }

    /**
     * 요거 처음 작성할 땐, One To One 분석 중 , LAZY 로딩 관련해서 List도 null하겐 절대 안가져 올 것 같다는 생각이 들어서 테스트 코드를 작성 해 봄.
     * 이럴 줄 알았으면 No-Arg에 new ArrayList를 안했을테니까.
     */
    @Nested
    @DisplayName("SELECT")
    class SELECT{
        /**
         * 시나리오
         *
         * 1. 팀만 생성함
         *
         * 2. 그 팀이 Member를 조회하면 묶인 멤버가 하나도 없을 것이다.
         *
         * 3. 이때 그 멤버리스트는 null인가 empty인가?
         */
        @Test
        @DisplayName("List는 null인가 empty인가?") // empty이다.
        void is_list_null_or_empty(){
            Team team = new Team();
            team.setId(1L);
            team.setName("Team1");

            em.persist(team);
            em.flush();
            em.clear();

            Team foundTeam = em.find(Team.class, 1L);
            List<Member> members = foundTeam.getMembers();

            System.out.println(divider);
            System.out.println(members);
        }
    }
}
