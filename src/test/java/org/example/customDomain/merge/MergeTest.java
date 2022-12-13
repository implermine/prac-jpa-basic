package org.example.customDomain.merge;

import org.example.BaseCondition;
import org.example.relation.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MergeTest extends BaseCondition {

    /**
     * em.persist()는 SELECT 쿼리를 INSERT 전에 안날려보는데, em.merge()는 날리는 문제에 대해서 inspect
     */
    @Test
    @DisplayName("save() 메서드 호출 시, isNew()가 false라면 merge()가 호출된다. 이때, merge의 스펙에 따른 쿼리형태를 알아본다.")
    // 그리고 그 스펙은 https://stackoverflow.com/questions/4509086/what-is-the-difference-between-persist-and-merge-in-jpa-and-hibernate/4509389#4509389
    // 여기서 확인 가능하다.
    void test(){
        //given
        Member member = new Member(1L,"memberA");
        //when
        em.merge(member); // merge만 해도, @Id가 채워져있는 Entity는 flush 전에 SELECT 쿼리를 날린다.

        /**
         * DefaultMergeEventListner.onMerge() 메서드의 하단부, switch문을 살펴보면, DETACHED 상태로 판정됨을 알 수 있는데,
         * 다음 테스트에서 DETACHED 상태의 entity를 강제로 생성해보고, 이와같이 merge 할때 SELECT 쿼리를 날리는지 확인해본다.
         */

        //then
        em.flush();
    }

    @Test
    @DisplayName("SELECT query fires even on detached entity")
    void test2(){
        //given
        Member member = new Member(1L,"memberA");
        em.persist(member);
        super.line("persist");
        em.detach(member); // detach는 PersistenceContext로부터의 Entity 삭제도 야기한다. 그러나 DB에 삭제쿼리를 날리는 행위는 아니다.
        // https://stackoverflow.com/a/18515524/16901221
        super.line("detach");
//        boolean contains = em.contains(member);
//        System.out.println(contains); // false

        // now, Member is detached state, but Entity Information is already registered in PersistenceContext, No

        Member mergedMember = em.merge(member); // is query fires..? yes

        /**
         * https://tech.junhabaek.net/hibernate-jpa-entitymanager-%ED%95%B5%EC%8B%AC-%EA%B8%B0%EB%8A%A5-%EC%A0%95%EB%A6%AC-3d0d9ff439a2#2b32
         * 의 `Merge 기본 동작` 에 따르면,
         * @Id에 데이터가 존재하고, Persistence Context에 해당 id의 엔티티가 존재하면,
         * 4번으로 바로 넘어간다, 즉 SELECT 쿼리를 날리지 않는다고 생각해도 됩니다. 이를 test3()에서 구현합니다.
         */

        em.flush();
    }

    @Test
    @DisplayName("Merge 기본 동작")
    void test3(){
        Member member = new Member(1L,"memberA");
        em.persist(member);
        member.setUsername("altered Member name");
        super.line("persist");

        Member mergedMember = em.merge(member); // is query fires..? no

        em.flush();

        // 이번엔 flush 타이밍에 INSERT, UPDATE 쿼리가 나갔다,
        // 생성되는 지연 쓰기 쿼리 저장소에서 각 연산마다 보간을 수행하는것이 아니므로,
        // 순번대로, INSERT, UPDATE 쿼리가 각각 발생했다.
    }


}
