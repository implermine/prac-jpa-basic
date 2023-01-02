package org.example.customDomain.DoesEmClearDisableINSERTquery;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * em.clear() 시 , 쓰기 지연 저장소에 있는 INSERT 쿼리도 없어짐? - YES
 */
public class TestEntry extends BaseCondition {

    @Test
    @DisplayName("em.clear() 시 , 쓰기 지연 저장소에 있는 INSERT 쿼리도 없어짐? - YES")
    void test(){
        Member member = new Member(1L, "memberA");

        em.persist(member);

        em.clear();

        em.flush();
    }
}
