package org.example.customDomain;

import org.example.BaseCondition;
import org.example.relation.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PersistTwiceTest extends BaseCondition {

    @Test
    @DisplayName("이미 DB에 특정 key로 데이터가 존재할때 다시 persist-flush 하면 어떻게 되는지")
    void test(){

        //given
        // Team 미리 저장해두고
        // clear 한 다음에
        // 다시 ID는 그대로인 다른 Name 집어넣으려하면 UPDATE 쿼리?
        Team team = new Team();
        team.setName("teamA");
        team.setId(1L);

        em.persist(team);
        em.flush();
        em.clear();

        //when
        Team team2 = new Team();
        team2.setName("teamB");
        team2.setId(1L);

        em.persist(team2);
        tx.commit();

        /**
         * Unique Key 에러 발생
         */

    }
}
