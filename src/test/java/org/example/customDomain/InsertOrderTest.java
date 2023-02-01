package org.example.customDomain;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.example.relation.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InsertOrderTest extends BaseCondition {

    /**
     * 자동정렬 안됌.
     */
    @Test
    @DisplayName("INSERT 순서가 em.persist()에도 관련있다. 트랜잭션 관련 쓰기 지연 저장소에 넣는 순서는 queue 처럼 선입선출이다.") // -> 아님, sorting 됨.
    /**
     * Debugging으로 순차 확인
     */
    void test(){
        Team team1 = new Team();
        team1.setId(1L);
        team1.setName("team1");

        Member member1 = new Member();
        member1.setId(1L);
        member1.setUsername("member1");
        member1.setTeam(team1);

        /**
         * 연관관계 미주인 -> 연관관계 주인 순으로 INSERT를 해야되지만
         * em.persist() 이후, 쓰기 지연 저장소에 있는 INSERT 쿼리를 JPA가 알잘딱해서 Queing이 아니라, 우선순위를 채택하여 넣는지 테스트
         */
        line("before persist member1");
        em.persist(member1);
        line("after persist member1");

        line("before persist team1");
        em.persist(team1);
        line("after persist team1");


        line("before flush");
        em.flush();
        line("after flush");



        /**
         * em.persist(member1)
         * -> member1 영속화 시도 (쓰기 지연 저장소에 INSERT 쿼리 저장보다 먼저),
         * team1이 비영속이므로, SELECT 쿼리 fire
         * team1 영속화 불가
         * 쓰기 지연 저장소에 INSERT 쿼리 저장 (team = null로)
         *
         * em.persist(team1)
         * -> team1을 쓰기 지연 저장소에 INSERT 쿼리 저장, 영속화 시도. 성공
         *
         * em.flush()
         * -> 쓰기 지연 저장소에 queuing 된 순서대로 INSERT 쿼리를 날림,
         * 먼저 Member의 team이 null인 상태로 INSERT
         * 그 다음 Team INSERT
         *
         * 그런데, flush를 수행하며 Dirty Checking을 하던 중, member1의 최초 영속화 타이밍인 em.persist(member1)에서 찍은 스냅샷은 team = null이지만
         * 객체 층에선 team1 이므로 Dirty Checking이 수행되며 UPDATE 쿼리가 나감.
         *
         * 즉, 여기서 알 수 있는건 3가지 정도가 있다.
         *
         * 1. 쓰기 지연 저장소는 sort() 함수를 통해 소팅된다.
         * 2. 비영속 부모 객체를 가진 Member는 영속화 시, 쓰기지연저장소에 UPDATE 쿼리도 남긴다.
         */


    }
}
