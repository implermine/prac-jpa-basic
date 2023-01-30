package org.example.jpql;

import org.assertj.core.api.Assertions;
import org.example.BaseCondition;
import org.example.relation.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class JPQLTest extends BaseCondition {

    @Test
    @DisplayName("스칼라 타입 조회")
    void select_scalar(){
        Member member = new Member(1L,"memberA");
        em.persist(member);
        em.flush();
        em.clear();

        String username = em.createQuery("SELECT m.username FROM Member m WHERE m.id = :id", String.class)
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(username).isEqualTo("memberA");
    }
}
