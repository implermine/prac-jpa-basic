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
    void test(){

        Member_OneToOne member = new Member_OneToOne();
        member.setId(3L);
        member.setUsername("member1");

        Locker_OneToOne locker = new Locker_OneToOne();
        locker.setId(7L);
        locker.setName("locker1");

        member.setLocker(locker);

        em.persist(locker);
        em.persist(member);

        System.out.println("\n\n ====== Before Flush ===== \n\n");
        System.out.println("Expect 2 Insert Query\n ");
        em.flush();
        System.out.println("\n\n ====== Before Flush ===== \n\n");
        em.clear();

        System.out.println("\n\n Before Find \n\n");
        System.out.println("Expect 1 force Join Query \n\n");
        Locker_OneToOne foundLocker = em.find(Locker_OneToOne.class, 7L);
        System.out.println("\n\n After Find \n\n");
    }

    @Test
    @DisplayName("영속화 되어있지 않은 Foreign Entity는 INSERT 전에 SELECT를 수행함")
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
