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
        Board board2 = new Board();
        Board board3 = new Board();




        System.out.println("=== Before Persist Board1");
        // 여긴 inititation 이라 한번 부르고
        em.persist(board);
        System.out.println("=== After Persist Board1");

        System.out.println("===Before Persist board2");
        // 여기선 실제로 allocationSize만큼 밀어야되서 부르고
        em.persist(board2);
        System.out.println("===After Persist board2");

        System.out.println("===Before Persist board3");
        // 여긴 안부름
        em.persist(board3);
        System.out.println("===After Persist board3");


        System.out.println("board.getId() = " + board.getId());
        System.out.println("board2.getId() = " + board2.getId());
        System.out.println("board3.getId() = " + board3.getId());





        // 이곳에서 추가 시퀀스 조회 쿼리가 발생하지 않아야함, When 시퀀스 allocationSize > 1 일 때,




        System.out.println("===Before Flush");
        em.flush();
        System.out.println("===After Flush");

        tx.rollback();
    }
}
