package org.example.customDomain.common;

import org.example.BaseCondition;
import org.example.common.Corporation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 영속화되지 않은 객체 삭제
 * -> 불가
 */
public class DeleteNotPersistedEntityTest extends BaseCondition {

    /**
     * 이 케이스에서 삭제가 불가한 이유는 corp1이 detached entity이기때문이다.
     *
     * 애초에 em을 통해선 entity만 삭제할 수 있으므로 (deleteById가 없음)
     *
     * JPA는 deleteById를 할때도 entity를 find해서 삭제하기때문에, em.removeById는 존재할 수 없기에
     *
     * 기능상으로도 Persist되지않은 Entity를 삭제할 수 없으며,
     *
     * detached entity는 더더욱 그렇다.
     */
    @Test
    @DisplayName("영속화되지 않은 객체 삭제") // -> 불가
    void test(){

        Corporation corp1 = new Corporation(1, "corp1");
        em.persist(corp1);
        em.flush();
        em.clear();
        line("after scenario");

        em.remove(corp1);
        //java.lang.IllegalArgumentException: Removing a detached instance org.example.common.Corporation#1<-(ID)
        line("after remove");

        em.flush();
        line("after flush");
    }
}
