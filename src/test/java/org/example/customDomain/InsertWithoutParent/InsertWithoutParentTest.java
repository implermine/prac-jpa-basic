package org.example.customDomain.InsertWithoutParent;

import org.example.BaseCondition;
import org.example.customDomain.InsertWithoutParent.model.Child_insertWithoutParent;
import org.example.customDomain.InsertWithoutParent.model.Parent_inserWithoutParent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * insertable=false & updatable=false 를 이용한 Parent없이 Child만 INSERT 테스트
 */
public class InsertWithoutParentTest extends BaseCondition {

    @Test
    @DisplayName("insertable=false & updatable=false 를 이용한 Parent없이 Child만 INSERT 테스트")
    void test1(){
        Child_insertWithoutParent child = new Child_insertWithoutParent();
        child.setChildId(1L);
        child.setName("child 1");
        child.setParentId(1L);
        em.persist(child);
        em.flush();
        em.clear();


        Parent_inserWithoutParent parent = new Parent_inserWithoutParent();
        parent.setParentId(1L);
        parent.setName("parent A");
        em.persist(parent);
        em.flush();
        em.clear();

        Child_insertWithoutParent foundChild = em.find(Child_insertWithoutParent.class, 1L);
        Long parentId = foundChild.getParentId();
        System.out.println(parentId);
        line("parentId fetch 하는것까지 parent 에 조인쿼리 미발생 예상");

        Parent_inserWithoutParent foundParent = foundChild.getParent();
        System.out.println(foundParent.getClass());
        line("class는 proxy 예상");

        String name = foundParent.getName();
        System.out.println(name);
        line("name은 parent A 예상 및 join쿼리 정상발생 예상");

    }
}
