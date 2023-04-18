package org.example.relation;

import org.example.BaseCondition;
import org.example.relation.onetoone.Book_OneToOne;
import org.example.relation.onetoone.Locker_OneToOne;
import org.example.relation.onetoone.Manuscript_OneToOne;
import org.example.relation.onetoone.Member_OneToOne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



public class OneToOneTest extends BaseCondition {

    /**
     * Member가 Locker를 reference한다.
     * Member는 Locker키를 들고있다.(LOCKER_ID)
     *
     *
     */
    @BeforeEach
    void scenario(){
        //멤버 1은 LOCKER1를 갖고있음
        Member_OneToOne member1 = new Member_OneToOne();
        member1.setId(1L);
        member1.setUsername("member1");

        //멤버 2는 LOCKER2가 없음
        Member_OneToOne member2 = new Member_OneToOne();
        member2.setId(2L);
        member2.setUsername("member2");

        //멤버 3은 LOCKER의 키를 갖고 있으나 실제로 라커는 없음
        Member_OneToOne member3 = new Member_OneToOne();
        member3.setId(3L);
        member3.setUsername("member3");

        Locker_OneToOne locker1 = new Locker_OneToOne();
        locker1.setId(1L);
        locker1.setName("locker1");

        Locker_OneToOne locker3 = new Locker_OneToOne();
        locker3.setId(3L);
        locker3.setName("locker3");

        //멤버1에게 라커1 키 주기
        member1.setLocker(locker1);
        member3.setLocker(locker3);

        em.persist(locker1);
        em.persist(locker3);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);

        em.flush();
        em.clear();

        Locker_OneToOne willDeleteLocker3 = em.find(Locker_OneToOne.class, 3L);
        em.remove(willDeleteLocker3);
        em.flush();
        em.clear();

        //=======================================

        Manuscript_OneToOne manuscript1 = new Manuscript_OneToOne();
        manuscript1.setId(1L);

        Book_OneToOne book1 = new Book_OneToOne();
        book1.setId(101L);

        em.persist(book1);

        manuscript1.setBook(book1);

        em.persist(manuscript1);
        em.flush();
        em.clear();

        line("AFTER SCENARIO");
    }


    @Test
    @DisplayName("일대일 관계에서 주(FK를 가진측)가 부를 호출할 때, 부가 있는경우")
    /**
     * OneToOne 관계에서, FK를 갖지 않은 `연관관계 미주인` 측에서 `연관관계 주인` 즉, FK를 가진측을 LAZY하게 호출 할 수 `없다`.
     */
    void test(){

        // 멤버1은 라커1 갖고있음.
        Member_OneToOne member1 = em.find(Member_OneToOne.class, 1L);

        line("before locker touch");
        System.out.println(member1.getLocker().getName());
        line("after locker touch");
    }

    @Test
    @DisplayName("일대일 관계에서 주(FK를 가진측)가 부를 호출할 때, 주가 가진 부 key가 없을 때 (null일 때)")
    /**
     * 그냥 행복하게 해당 값 null로 채워짐. 아이 행복해라
     */
    void test2(){

        // 멤버1은 라커1 갖고있음.
        Member_OneToOne member2 = em.find(Member_OneToOne.class, 2L);

        line("before locker touch");
        System.out.println(member2.getLocker().getName());
        line("after locker touch");
    }

    @Test
    @DisplayName("일대일 관계에서 주(FK를 가진측)가 부를 호출할 때, 주가 가진 부 key는 있으나, 실제로 그 테이블엔 없을때 (반정규화)")
    /**
     * EntityNotFoundException
     */
    void test3(){

        // 멤버1은 라커1 갖고있음.
        Member_OneToOne member3 = em.find(Member_OneToOne.class, 3L);

        line("before locker touch");
        System.out.println(member3.getLocker().getName());
        /**
         * EntityNotFoundException: Unable to find org.example.relation.onetoone.Locker_OneToOne with id 3
         */
        line("after locker touch");
    }

    @Test
    @DisplayName("일대일 관계에서 부(FK를 가지지 않은 측)에서 주를 호출할 때, 주가 있는 경우 (LAZY가 안되는걸 보기위함)")
    /**
     * LAZY 안됌
     */
    void test4(){
        Locker_OneToOne locker1 = em.find(Locker_OneToOne.class, 1L);

        line("before Member touch");
        System.out.println(locker1.getMember().getUsername());
        line("after Member touch");
    }

    /**
     * 식별(identifying) 관계일땐 어떨까?
     * 식별 관계 + 반정규 테이블일 경우 virtual FK는 양측 모두 들고있다.
     *
     * 1.일대일 관계에서 주(FK를 가진측)가 부를 호출할 때, 부가 있는경우
     * --> 가장 COMMON 하고, LAZY도 가능할 것 같은데
     *
     * 2.일대일 관계에서 주(FK를 가진측)가 부를 호출할 때, 주가 가진 부 key가 없을 때 (null일 때)
     * --> 불가능하다, 식별관계이므로, key(FK)가 없다는것은 본인의 PK도 없다는것이기에
     *
     * 3.일대일 관계에서 주(FK를 가진측)가 부를 호출할 때, 주가 가진 부 key는 있으나, 실제로 그 테이블엔 없을때 (반정규화)
     * --> 가능하다.
     *
     * 4.일대일 관계에서 부(FK를 가지지 않은 측)에서 주를 호출할 때, 주가 있는 경우 (LAZY가 안되는걸 보기위함)
     *
     */

    //@OneToOne subentity에서 LAZY없이 로딩할 수 있는 방법이 있다고?
    @Test
    void test5(){
        Book_OneToOne book = em.find(Book_OneToOne.class, 101L);

        System.out.println("hey");
    }

}
