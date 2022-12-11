package org.example.cascade;

import org.example.BaseCondition;
import org.example.cascade.model.cascaded.Child;
import org.example.cascade.model.cascaded.Parent;
import org.example.cascade.model.notCascaded.NotCascadedChild;
import org.example.cascade.model.notCascaded.NotCascadedParent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;

public class CascadeTest extends BaseCondition {

    @Test
    @DisplayName("Cascade가 없을때 Default 행동에 대해")
    void should_not_cascaded() {
        /**
         * <시나리오>
         *
         * NotCascaded 케이스 이므로,
         * 부모를 먼저 Insert 하고,
         * 자식을 Insert한다.
         *
         * flush의 순서를 통해 강제로,
         * 자식을 먼저 밀어넣고, 부모를 밀어넣는다면,
         * Update 쿼리를 이용하여 자식 레코드의 FK를 수정한다.
         *
         * 그리하여 이 테스트를 통해 알아 볼 것은, Child가 Parent를 필드로 소유하고, Parent가 List로 Child를 소유하더라도,
         * `자동으로` flush를 통해 DB에 child까지 INSERT 되지 `않는것`을 볼 예정이다.
         *
         * [result]
         *
         * Parent만 insert 되었다.
         *
         */
        NotCascadedParent parentA = new NotCascadedParent("parentA");

        NotCascadedChild childA = new NotCascadedChild("childA");
        NotCascadedChild childB = new NotCascadedChild("childB");

        parentA.addChildBoth(childA);
        parentA.addChildBoth(childB);

        em.persist(parentA);

        em.flush();
    }

    @Test
    @DisplayName("cascade를 사용한, 같이 insert")
    void should_cascaded_that_insertion_occurs_simultaneously(){
        /**
         * 시나리오
         *
         * CASCADE를 사용하였으므로, 부모객체만 밀어넣어도 같이 INSERT 될 것이다.
         *
         * 완
         */
        Parent parentA = new Parent("parentA");

        Child childA = new Child("childA");
        Child childB = new Child("childB");

        parentA.addChildBoth(childA);
        parentA.addChildBoth(childB);

        em.persist(parentA);

        em.flush();
    }







    /**
     * 코어하게 테스트하기 위해 Parent의 childList를 다음과 같이 설정
     *
     * @OneToMany(mappedBy = "parent",orphanRemoval = true)
     *
     * hibernate 버그라고 함. CASCADE없이 사용할 경우 orphanRemoval 비정상 작동
     */
    @Test
    @DisplayName("orphan removal test")
    void orphanRemoval(){
        /**
         * 시나리오
         *
         * parent가 child를 삭제 한 경우(list에서)
         *
         * 부모가 자식을 버린 경우이고, orphanRemoval이 켜져 있으므로, 자식 엔티티 삭제 쿼리가 발생한다.
         *
         * 강의케이스
         */


        /**
         * given
         */
        Parent parentA = new Parent("parentA");
        Child childA = new Child("childA");
        Child childB = new Child("childB");
        parentA.addChildBoth(childA);
        parentA.addChildBoth(childB);
        em.persist(parentA);
        em.persist(childA);
        em.persist(childB);
        em.flush();
        em.clear();

        /**
         * when
         */
        Parent foundParent = em.find(Parent.class, parentA.getId());
        foundParent.getChildList().remove(0);



        /**
         * then
         *
         * childA의 Delete 쿼리 발생 예상 ---> 발생하지 않음.
         */
        System.out.println(lineDivider);
        em.flush();
    }

    /**
     * 하이버네이트 구현 문제라는것을 파악 후, orphanRemoval과 CASCADE-PERSIST 옵션만 키고 orphanRemoval 테스트와 같은 경우로 테스트 해봄
     *
     * @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL,orphanRemoval = true)
     *
     */
    @Test
    @DisplayName("orphanRemoval, and cascadeType을 PERSIST로")
    void orphanRemoval2(){
        /**
         * 시나리오
         *
         * parent가 child를 삭제 한 경우(list에서)
         *
         * 부모가 자식을 버린 경우이고, orphanRemoval이 켜져 있으므로, 자식 엔티티 삭제 쿼리가 발생한다.
         *
         * 강의케이스
         */


        /**
         * given
         */
        Parent parentA = new Parent("parentA");
        Child childA = new Child("childA");
        Child childB = new Child("childB");
        parentA.addChildBoth(childA);
        parentA.addChildBoth(childB);
        em.persist(parentA);
        em.persist(childA);
        em.persist(childB);
        em.flush();
        em.clear();

        /**
         * when
         */
        Parent foundParent = em.find(Parent.class, parentA.getId());
        foundParent.getChildList().remove(0);



        /**
         * then
         *
         * childA의 Delete 쿼리 발생 예상 ---> 발생함.
         */
        System.out.println(lineDivider);
        em.flush();
    }

    /**
     * cascadeType=PERSIST 테스트
     *
     * PERSIST로 제한 해 두 었으니, DELETE 는 같이 발생하지 않아야한다.
     *
     * Parent를
     * @OneToMany(mappedBy = "parent",cascade = CascadeType.PERSIST)
     * 로 설정
     *
     */
    @Test
    @DisplayName("remove없이 persist만")
    void should_persist_simultaneously_but_no_removal(){
        /**
         * given
         */
        Parent parentA = new Parent("parentA");
        Child childA = new Child("childA");
        Child childB = new Child("childB");
        parentA.addChildBoth(childA);
        parentA.addChildBoth(childB);
        em.persist(parentA);
        em.flush();
        // PERSIST니까 여기서 insert 쿼리 3개 발생
        System.out.println(1+lineDivider);

        em.clear();
        Parent foundParent2 = em.find(Parent.class, parentA.getId());
        em.remove(foundParent2);

        Assertions.assertThrows(PersistenceException.class, () -> em.flush());
        System.out.println(3+lineDivider);
        // 여기선 fk 때문에 DB에서 쿼리익셉션 날 것 같긴한데, 일단 기능 지원 여부 확인 -> 쿼리익셉션 발생함 ConstraintViolationException
    }


    /**
     * Parent의 cascade를 다음과 같이 변경
     *
     * @OneToMany(mappedBy = "parent",cascade = CascadeType.REMOVE)
     */
    @Test
    @DisplayName("그럼 REMOVE 설정해두면 괜찮나? `remove없이 persist만`의 추가 테스트")
    void testRemove() {
        /**
         * given
         */
        Parent parentA = new Parent("parentA");
        Child childA = new Child("childA");
        Child childB = new Child("childB");
        parentA.addChildBoth(childA);
        parentA.addChildBoth(childB);
        em.persist(parentA);
        em.persist(childA); //
        em.persist(childB); // 이렇게 2개 이제 해줘야함 remove만 있으니
        em.flush();
        em.clear();
        // PERSIST니까 여기서 insert 쿼리 3개 발생
        System.out.println(1 + lineDivider);

        em.clear();
        Parent foundParent2 = em.find(Parent.class, parentA.getId());
        em.remove(foundParent2);
        em.flush();
        System.out.println(2 + lineDivider);
        // 여기선 fk 때문에 DB에서 쿼리익셉션 날 것 같긴한데 -> 이제 안남 - > 안남
    }
}
