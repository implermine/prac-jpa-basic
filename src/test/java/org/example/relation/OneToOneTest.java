package org.example.relation;

import org.example.relation.onetoone.Locker_OneToOne;
import org.example.relation.onetoone.Member_OneToOne;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class OneToOneTest {

    @Test
    @DisplayName("OneToOne Test for 강제 즉시 로딩")
    /**
     * WHEN
     *
     * 연관관계를 관리하지 않는 측은,
     * Default론 Lazily하게 Loading된다.
     *
     * 그러나, 1:1 관계에선, Eager하다.
     */
    void test(){

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

        System.out.println("\n\n=================================================================================\n\n");
        System.out.println("Locker만 찾아오는게 아니라 Join 해서 가져오는것을 예상\n\n");
        Locker_OneToOne foundLocker = em.find(Locker_OneToOne.class, 7L);
        System.out.println("\n\n=================================================================================\n\n");

    }

    @Test
    @DisplayName("영속화 되어있지 않은 Foreign Entity는 UPDATE를 날림")
    void test2(){

        Member_OneToOne member = new Member_OneToOne();
        member.setId(3L);
        member.setUsername("member1");

        Locker_OneToOne locker = new Locker_OneToOne();
        locker.setId(7L);
        locker.setName("locker1");

        member.setLocker(locker);

        em.persist(member);
        em.persist(locker);


        System.out.println("\n\n ====== Before Flush ===== \n\n");
        System.out.println("Expect 2 Insert Query\n ");
        em.flush();
        System.out.println("\n\n ====== Before Flush ===== \n\n");
        em.clear();

        tx.commit();
    }
















    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public OneToOneTest() {
        emf = Persistence.createEntityManagerFactory("hello");
    }

    @BeforeEach
    public void setUp(){
        em = emf.createEntityManager();
        tx = em.getTransaction();
        tx.begin();
    }

    @AfterEach
    public void tearDown(){
//        tx.rollback();
        em.close();
    }
}
