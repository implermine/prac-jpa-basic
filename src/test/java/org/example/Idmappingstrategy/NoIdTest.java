package org.example.Idmappingstrategy;

import org.example.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class NoIdTest {

    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public NoIdTest() {
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
    @DisplayName("기본 키 내가 넣으면 insert 쿼리 바로 안나가는거 확인")
    void test(){
        Member member = new Member();
        member.setId(1L);

        System.out.println("=== Before persist");
        em.persist(member);
        System.out.println("=== After persist");


        System.out.println("=== Before commit");
        tx.commit();
        System.out.println("=== After commit");

    }
}
