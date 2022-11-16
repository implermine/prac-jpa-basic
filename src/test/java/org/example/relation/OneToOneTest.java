package org.example.relation;

import org.example.BaseCondition;
import org.example.relation.onetoone.Locker_OneToOne;
import org.example.relation.onetoone.Member_OneToOne;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



public class OneToOneTest extends BaseCondition {

    @Test
    @DisplayName("OneToOne Test for 강제 즉시 로딩")
    /**
     * 1. OneToOne 관계에서, FK를 갖지 않은 `연관관계 미주인` 측에서 `연관관계 주인` 즉, FK를 가진측을 LAZY하게 호출 할 수 `없다`.
     * 2. Proxying 되어있는 객체를 unwrap 할때도, 하이버네이트 1차캐시에서 먼저 @Id로 객체를 탐색한다. // ->
     * //-> 요거 확인해보려면 밑에 Locker에선 Member를 Lazy하게 Loading할 수 없으므로 쿼리가 나가야되는데 실제로 돌려보면 Before Find Locker와
     * After Find Locker 사이에 쿼리는 단 한번이다. 즉 강제프록시 로딩을 할 때, 쿼리가 추가로 나가지 않고 1차캐시에서 찾았다.
     */
    void test(){

        /**
         * given
         *
         * Member 세팅하고
         * Locker 세팅해서, Member에 Locker set
         */
        // 연관관계 주인 (주 테이블)
        Member_OneToOne member = new Member_OneToOne();
        member.setId(3L);
        member.setUsername("member1");
        // 연관관계 미주인 (대상 테이블)
        Locker_OneToOne locker = new Locker_OneToOne();
        locker.setId(7L);
        locker.setName("locker1");
        // 연관관계 세팅
        member.setLocker(locker);
        // 미 연관관계인 친구를 먼저 push , 영속화 되어있지 않은 친구를 먼저 넣으면 null로 넣었다가 나중에 UPDATE로 바꾼다.
        em.persist(locker);
        em.persist(member);
        em.flush();
        em.clear();


        /**
         * Member SELECT
         */
        System.out.println(lineDivider);
        System.out.println("Before Find Member");
        /**
         * Member는 Lazily Locker를 호출함. Proxying 성공
         */
        Member_OneToOne foundMember = em.find(Member_OneToOne.class, 3L);
        System.out.println("After Find Member");
        System.out.println(lineDivider);
        System.out.println(lineDivider);
        System.out.println("Before Find Locker");
        Locker_OneToOne foundLocker = em.find(Locker_OneToOne.class, 7L);
        System.out.println("After Find Locker");
        System.out.println(lineDivider);


    }
}
