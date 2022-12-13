package org.example.relation;

import org.example.BaseCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ManyToOneTest extends BaseCondition {
    @Test
    @DisplayName("ManyToOne 객체 참조했을 때, INSERT 쿼리 잘 나가나")
    /**
     * UPDATE 쿼리 없이 나가는 가장 좋은 이상적인 방법.
     * OneToManyTest.one_to_many_insert_test 가 가장 절망적인 방법.
     */
    void check_two_distinct_insert_query(){

        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(1L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        System.out.println("Before Persist Team");
        // 쿼리 미발생
        em.persist(team);
        System.out.println("After Persist Team");

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        System.out.println("Before Persist Member");
        // 쿼리 미발생
        em.persist(member);
        System.out.println("After Persist Member");

        System.out.println("Before flush");
        // 쿼리 발생
        em.flush();
        System.out.println("After flush");
    }

    @Test
    @DisplayName("ManyToOne 객체 호출했을 때, SELECT 쿼리 조인해서 잘 나가나")
    void check_select_query(){

        //given

        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(1L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        // 쿼리 미발생
        em.persist(team);

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        // 쿼리 미발생
        em.persist(member);

        // 쿼리 발생
        em.flush();
        em.clear();

        System.out.println("Before Find");
        Member foundMember = em.find(Member.class, 1L);
        System.out.println("After Find");
    }

    @Test
    @DisplayName("ManyToOne 객체 호출했을 때, UPDATE 쿼리 잘 나가나")
    void check_update_query(){

        //given

        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(1L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        // 쿼리 미발생
        em.persist(team);

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        // 쿼리 미발생
        em.persist(member);

        // 쿼리 발생
        System.out.println("Before First Flush");
        em.flush();
        System.out.println("After First Flush");


        member.getTeam().setName("modified Team Name");

        System.out.println("Before Second Flush");
        em.flush();
        System.out.println("After Second Flush");

    }



    @Test
    @DisplayName("영속화되지 않은 부모객체(Team)을 set하고 persist 하면 어떻게 되는지")
    /**
     * 영속화 되지 않은 부모객체를 SELECT로 찾아서 저장한다.
     */
    void check_setting_transient_parent_entity(){
        /**
         * scenario
         *
         * 1. Team 저장 (persist -> flush)
         * 2. em.clear() -> 비영속
         *
         * 3. Member 에 `the` Team set
         *
         * 4. Member 저장 (persist -> flush)
         *
         *
         */

        //1
        Team team1 = new Team();
        team1.setId(1L);
        team1.setName("team1");
        em.persist(team1);
        em.flush();

        //2
        em.clear();

        //3
        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("member1");
        member1.setTeam(team1);

        //4
        System.out.println(divider);
        em.persist(member1);
        em.flush();
    }

    @Test
    @DisplayName("ManyToOne 객체 호출했을 때, UPDATE 쿼리 잘 나가나, FOREIGN KEY 대상으로")
    void check_update_query_for_foreign_key() {

        /**
         * given
         *
         * 1. Team1 저장 (flush)
         * 2. Team2 저장 (flush)
         * 3. em.clear()
         * 4. ㅇ
         */
        Team savedTeam = new Team();
        savedTeam.setName("SavedTeam");
        savedTeam.setId(1L);

        em.persist(savedTeam);

        System.out.println("/// Expect Insert Query");
        em.flush();
        em.clear(); // savedTeam detached
        System.out.println("///");


        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(2L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        // 쿼리 미발생
        em.persist(team);

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        // 쿼리 미발생
        em.persist(member);

        // 쿼리 발생
        System.out.println("Before First Flush");
        System.out.println("/// Expect 2 insert query");
        em.flush();
        System.out.println("///");
        System.out.println("After First Flush");

        Team savedTeam2 = em.find(Team.class, 1L);
        member.setTeam(savedTeam2); // 팀을 기존팀으로 바꾼거지 이거 만약에 detached 였으면, 다시 조회해서 영속화하네.

        System.out.println("Before Second Flush");
        System.out.println("/// Expect 1 update query");
        em.flush();
        System.out.println("///");
        System.out.println("After Second Flush");

        tx.commit();

    }
}
