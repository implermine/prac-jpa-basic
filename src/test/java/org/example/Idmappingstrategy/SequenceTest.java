package org.example.Idmappingstrategy;

import org.example.Board;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class SequenceTest {

    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public SequenceTest() {
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
    @DisplayName("시퀀스 잘 만들어지나 테스트")
    void test(){
        Board board = new Board();
        em.persist(board);

        System.out.println("===Before print");
        System.out.println("board.id = " + board.getId());
        System.out.println("===After print");


        Board board2 = new Board();
        em.persist(board2);

        System.out.println("===Before print2");
        System.out.println("board2.id = " + board2.getId());
        System.out.println("===After print2");

        System.out.println("===Before Commit");
        tx.commit();
        System.out.println("===After Commit");
    }
}
