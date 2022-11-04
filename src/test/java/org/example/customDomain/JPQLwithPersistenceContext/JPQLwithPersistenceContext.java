package org.example.customDomain.JPQLwithPersistenceContext;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.example.SomeMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.List;

public class JPQLwithPersistenceContext extends BaseCondition {

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


}
