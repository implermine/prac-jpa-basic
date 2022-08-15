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

    @Test
    @DisplayName("기본 키 내가 넣으면 insert 쿼리 바로 안나가는거 확인, 쓰기 지연 저장소는 스택처럼 쌓이는것이지 서로 INSERT-DELETE 끼리 보간하거나 그런 관계는 없다")
    void test2(){
        Member member = new Member();
        member.setId(1L);

        System.out.println("=== Before persist");
        em.persist(member);
        System.out.println("=== After persist");

        System.out.println("=== Before remove");
        em.remove(member);
        System.out.println("=== After remove");



        System.out.println("=== Before flush");
        em.flush();

        /**
         * 여기서 `2번의 쿼리`가 날라가야 정상이다.
         *
         * Hibernate:
         *     insert
         *     into
         *         MEMBER
         *         (age, createdDate, description, lastModifiedDate, name, roleType, id)
         *     values
         *         (?, ?, ?, ?, ?, ?, ?)
         * Hibernate:
         *     delete
         *     from
         *         MEMBER
         *     where
         *         id=?
         *
         */
        System.out.println("=== After flush");

        tx.rollback();
    }
}
