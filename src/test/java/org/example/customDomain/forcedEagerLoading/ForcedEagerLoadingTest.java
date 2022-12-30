package org.example.customDomain.forcedEagerLoading;

import org.example.BaseCondition;
import org.junit.jupiter.api.Test;

public class ForcedEagerLoadingTest extends BaseCondition {

    @Test
    void test(){

        //given
        Member_Force memberA = new Member_Force(1L, "memberA");
        Team_Force team1 = new Team_Force(1L, "team1", 101L);
        memberA.setTeam(team1);
        em.persist(team1);
        em.persist(memberA);
        em.flush();
        em.clear();


        //when
        Member_Force foundMember = em.find(Member_Force.class, 1L);
        Team_Force team = foundMember.getTeam();// is proxied?
        System.out.println(team.getClass()); // no

        /**
         * Team_Force에 Serializable을 구현하지 않으면, 애초에 @Id에 fk가 걸려있지 않으므로 Proxying은 당연 불가하고,
         * 그 Entity를 어떤식으로 영속화 할 지도 Member 입장에선 모르기에, Serializable이 필요해보인다.
         *
         * 일단 lazy loading 안먹는건 확실하다.?
         *
         * 정리하자면, @Id를 referencedColumn으로 하지않으면 강제 EAGER이다.
         */


    }
}
