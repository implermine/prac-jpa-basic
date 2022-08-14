package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    @DisplayName("그냥 emf와 em 생성 로직")
    void show_emf_and_em(){

        /**
         * 엔티티 매니저 팩토리는 어플리케이션 로딩 시점에 하나만 만들어야 한다.
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        /**
         * 데이터베이스 커넥션을 하나 얻는다고 생각하면 됌.
         */
        EntityManager em = emf.createEntityManager();


        em.close();

        /**
         * WAS가 내려갈때 닫으면 됌
         */
        emf.close();
    }

    @Test
    @DisplayName("emf로 멤버 저장 로직")
    void test2(){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        Member member = new Member();
        member.setId(1L);
        member.setName("implermine");

        em.persist(member);

        tx.commit();

        em.close();

        emf.close();
    }

    @Test
    @DisplayName("emf로 멤버 저장 로직: 롤백포함")
    void test3(){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            Member member = new Member();
            member.setId(1L);
            member.setName("implermine");

            em.persist(member);


            tx.commit();


        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
    }

}