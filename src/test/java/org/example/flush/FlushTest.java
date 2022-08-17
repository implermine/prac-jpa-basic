package org.example.flush;

import org.assertj.core.api.Assertions;
import org.example.SomeMember;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class FlushTest {

    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public FlushTest() {
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
        em.close();
    }

    @Test
    @DisplayName("flush를 호출하면 commit 전에도 SQL 쿼리가 쓰기 지연 저장소에서 fire 되는지")
    void flushTest1(){
        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("회원1");


        em.persist(someMember); // 스냅샷 찍고, 엔티티 저장하고, key(ID) 저장하고, 쓰기 지연 저장소에 INSERT 쿼리 저장함

        System.out.println("=== Before Flush");
        em.flush(); // INSERT 쿼리 방출
        System.out.println("=== After Flush");

        System.out.println("=== Before commit");
        tx.commit(); //
        System.out.println("=== After commit");


        /**
         * === Before Flush
         * Hibernate:
         *     insert
         *     into
         *         Member
         *         (name, id)
         *     values
         *         (?, ?)
         * === After Flush
         * === Before commit
         * === After commit
         */
    }

    @Test
    @DisplayName("flush를 호출하면 1차 캐시가 비워지는지") // 비워지지 않음
    void flushTest2(){
        SomeMember someMember = new SomeMember();
        someMember.setId(1L);
        someMember.setName("회원1");


        em.persist(someMember); // 스냅샷 찍고, 엔티티 저장하고, key(ID) 저장하고, 쓰기 지연 저장소에 INSERT 쿼리 저장함

        em.flush(); // INSERT 쿼리 방출

        Assertions.assertThat(em.contains(someMember)).isTrue();
    }
}
