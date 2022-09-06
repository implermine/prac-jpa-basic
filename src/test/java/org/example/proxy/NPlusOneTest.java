package org.example.proxy;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.example.relation.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class NPlusOneTest extends BaseCondition {


    /**
     * N+1 첫번째 케이스 ( 알고있는 케이스와 다르다. EAGER 일때 발생하는 1:N의 1만 생각했었는데 리스트를 조회하는 경우를 생각못함)
     *
     * N:1 관계에서 Many List 조회, EAGER일 때,
     */
    @Test
    @DisplayName("EAGER, Many측의 List 조회")
    void test(){
        // given
        Team teamA = new Team();
        teamA.setId(1L);
        teamA.setName("teamA");

        Team teamB = new Team();
        teamB.setId(2L);
        teamB.setName("teamB");

        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("member1");

        Member member2 = new Member();
        member2.setId(2L);
        member2.setUsername("member2");

        Member member3 = new Member();
        member3.setId(3L);
        member3.setUsername("member3");


        // Member1 -> TeamA
        // Member2 -> TeamB
        // Member3 -> TeamA

        member1.setTeam(teamA);
        member2.setTeam(teamB);
        member3.setTeam(teamA);

        // 팀 먼저 영속화
        em.persist(teamA);
        em.persist(teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        em.flush();
        em.clear();

        System.out.println("\n\n\n=====================================================================================\n\n\n");


        // when
        // Member를 모두 찾는 쿼리가 나감, 그런데 EAGER라서, 각자를 모두 `각자` 다시 가져옴
        // 한번에 가져오는게 아니라 각자 가져온다는 점을 생각해야함
        /**
         * 즉, 다음과 같이
         * SELECT *
         * FROM TEAM
         * WHERE TEAM.ID IN (?,?,?,....?) 이렇게 가져올법도 한데,
         *
         * SELECT *
         * FROM TEAM
         * WHERE TEAM.ID = ?
         *
         * ...
         *
         * SELECT *
         * FROM TEAM
         * WHERE TEAM.ID = ?
         *
         * 이런식으로 여러번 가져옴
         *
         * 그 이유는 컬렉션은 JPA와 아무 상관이 없다고 생각해보자.
         *
         * 그렇다면, `각자` 라는것이 이해된다.
         */
        List<Member> memberList = em.createQuery("select m from Member m", Member.class).getResultList();


        // then
        /**
         * select
         *         team0_.ID as id1_20_0_,
         *         team0_.name as name2_20_0_
         *     from
         *         Team team0_
         *     where
         *         team0_.ID=?
         * =====================================
         * select
         *         team0_.ID as id1_20_0_,
         *         team0_.name as name2_20_0_
         *     from
         *         Team team0_
         *     where
         *         team0_.ID=?
         */

        //이런식으로 조회한 MemberList의 DISTINCT TEAM_ID 만큼 쿼리가 나감.
        // 그러나, 조회했을 때 MemberList가 비어있으면 N+1도 발생하지않음, 추가적으로 나갈 쿼리가 없으니
    }

    /**
     * N+1 두번째 케이스, Member에서 Team을 EAGER가 아닌 LAZY로 조회하게끔 했음(원복해야함)
     *
     * N:1 관계에서 Many List 조회, LAZY 일때,
     */
    @Test
    @DisplayName("LAZY, Many측의 List 조회")
    void test2(){
        // given
        Team teamA = new Team();
        teamA.setId(1L);
        teamA.setName("teamA");

        Team teamB = new Team();
        teamB.setId(2L);
        teamB.setName("teamB");

        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("member1");

        Member member2 = new Member();
        member2.setId(2L);
        member2.setUsername("member2");

        Member member3 = new Member();
        member3.setId(3L);
        member3.setUsername("member3");


        // Member1 -> TeamA
        // Member2 -> TeamB
        // Member3 -> TeamA

        member1.setTeam(teamA);
        member2.setTeam(teamB);
        member3.setTeam(teamA);

        // 팀 먼저 영속화
        em.persist(teamA);
        em.persist(teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        em.flush();
        em.clear();

        System.out.println("\n\n\n=====================================================================================\n\n\n");

        List<Member> memberList = em.createQuery("select m from Member m", Member.class).getResultList();

        /**
         * 예상쿼리? 없을거같은데 그냥, 일단은 아무 쿼리도 안나가고 TOUCH하면 나갈것 같은데,
         * member를 iter 하면서 어떻게 나갈지 예상해보면,
         * member에 있는 TEAM_ID로 조회해보고, 조회하기 전에 TEAM_ID가 영속성 컨텍스트에 존재하면
         * 쿼리 안날릴듯, 따라서 2개 발생함
         */

        for (Member member : memberList) {
            Team team = member.getTeam();
        }

    }

    /**
     * N+1 세번째 케이스, TEAM이 MEMBERLIST를 LAZY로 가지고 있을 때, N+1일것같지만 아닌것
     *
     * N:1 관계에서 Many List 조회, LAZY 일때,
     */
    @Test
    @DisplayName("LAZY, One측의 List가 아닌 One 조회 , List 조회하면 N(N+1) 이겠지 머 -> N(1)임")
    void test3(){
        // given
        Team teamA = new Team();
        teamA.setId(1L);
        teamA.setName("teamA");

        Team teamB = new Team();
        teamB.setId(2L);
        teamB.setName("teamB");

        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("member1");

        Member member2 = new Member();
        member2.setId(2L);
        member2.setUsername("member2");

        Member member3 = new Member();
        member3.setId(3L);
        member3.setUsername("member3");


        // Member1 -> TeamA
        // Member2 -> TeamB
        // Member3 -> TeamA

        member1.setTeam(teamA);
        member2.setTeam(teamB);
        member3.setTeam(teamA);

        // 팀 먼저 영속화
        em.persist(teamA);
        em.persist(teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        em.flush();
        em.clear();

        System.out.println("\n\n\n=====================================================================================\n\n\n");

        Team team = em.find(Team.class, 1L);

        List<Member> members = team.getMembers();

        System.out.println("\n\n 여기까지 추가 쿼리 안나감 \n\n");

        System.out.println("여기부터 추가쿼리 N+1, 3개의 추가 쿼리 예상 X TEAM_ID로 조회하면 되니까 1개임");
        for (Member member : members) {
            String username = member.getUsername();
        }


//        List<Member> memberList = em.createQuery("select t from Team t", Member.class).getResultList();

    }

    /**
     * N+1 네번째 케이스, TEAM이 MEMBERLIST를 EAGER로 가지고 있을 때 (설정값 확인), N+1일것같지만 아닌것
     *
     * N:1 관계에서 Many List 조회, LAZY 일때,
     */
    @Test
    @DisplayName("EAGER, One측의 List가 아닌 One 조회 , List 조회하면 N(N+1) 이겠지 머 -> N(1)임")
    void test4(){
        // given
        Team teamA = new Team();
        teamA.setId(1L);
        teamA.setName("teamA");

        Team teamB = new Team();
        teamB.setId(2L);
        teamB.setName("teamB");

        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("member1");

        Member member2 = new Member();
        member2.setId(2L);
        member2.setUsername("member2");

        Member member3 = new Member();
        member3.setId(3L);
        member3.setUsername("member3");


        // Member1 -> TeamA
        // Member2 -> TeamB
        // Member3 -> TeamA

        member1.setTeam(teamA);
        member2.setTeam(teamB);
        member3.setTeam(teamA);

        // 팀 먼저 영속화
        em.persist(teamA);
        em.persist(teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        em.flush();
        em.clear();

        System.out.println("\n\n\n=====================================================================================\n\n\n");

        List<Team> teamList = em.createQuery("select t from Team t where t.id = 1", Team.class).getResultList();

        Team team = teamList.get(0);

        List<Member> members = team.getMembers();

        System.out.println("\n\n 여기까지 추가 쿼리 나감이미, 왜냐면 EAGER니까 \n\n");
    }

    /**
     * 문제는 결국 List 조회이다. 혹은 1:1:N이 야기하는 N+1이다. List를 조회하지않고 1을 조회했다 하더라도 그에 연관된 Entity는 List(N)일 수 있으니까
     */

}
