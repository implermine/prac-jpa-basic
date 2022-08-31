package org.example;

import org.example.relation.Team;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CommonTest {

    @Test
    @DisplayName("영속화 되지 않은 객체도 DELETE 할 수 있는가? -> 불가능함")
    void test(){
        Team team = new Team();
        team.setId(1L);
        team.setName("팀1");

        em.persist(team);
        em.flush();
        em.clear();

        System.out.println("\n\n 여기서 DELETE 쿼리를 예상함 \n\n ");
        em.remove(team);
        em.flush();
    }


    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public CommonTest() {
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
