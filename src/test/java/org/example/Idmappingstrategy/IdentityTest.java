package org.example.Idmappingstrategy;

import org.example.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class IdentityTest {

    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public IdentityTest() {
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
    @DisplayName("영속성 컨텍스트에 의해 관리되려면 반드시 PK가 있어야한다. 따라서 IDENTITY 를 사용하면 트랜잭션을 지원하는 쓰기 지연을 활용할 수 없다.")
    void test(){
        Book book = new Book();
        book.setName("책 1");

        System.out.println("--- Before Persist");
        em.persist(book); // -> 따라서 이 시점에 INSERT 쿼리가 발생한다.
        System.out.println("--- After Persist");


        System.out.println("--- Before Commit(Flush) ");
        em.flush();
        System.out.println("--- After Commit(Flush) ");

        tx.rollback();
    }

}
