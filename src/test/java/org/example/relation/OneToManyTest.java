package org.example.relation;

import org.example.relation.onetomany.Member_;
import org.example.relation.onetomany.Team_;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * 1. 테이블의 일대다 관계는 항상 `다(N) 쪽에 외래키가 있음`
 * 따라서, 객체와 테이블의 차이 때문에 반대편 테이블의 외래 키를 관리하는 특이한 구조
 * @JoinColumn을 꼭 사용해야 함. 그렇지 않으면 조인 테이블 방식을 사용함(중간에 테이블을 하나 추가함)
 */
public class OneToManyTest {

    @Test
    @DisplayName("1:N 관계에서 1이 연관관계의 주인인 경우")
    void test(){
        Member_ member_ = new Member_();
        member_.setId(1L);
        member_.setUsername("member1");
        em.persist(member_);

        /**
         * 원래같앴으면 Team을 먼저 만들고 member를 그 다음 만든다음에,
         * member.setTeam(team) 해 줬을 테지만,
         * 이번엔 연관관계의 주인이 반대이니,
         * member 먼저 만들고 team을 setting 중.
         */
        Team_ team_ = new Team_();
        team_.setId(1L);
        team_.setName("team1");

        // 여기가 좀 애매해. 이게 되나?
        team_.getMembers().add(member_);
        em.persist(team_);

        System.out.println("\n ===================================== Before Flush ===================================== \n");
        System.out.println("===================================== Expect 2 Insert Query And 1 Update Query =====================================");
        em.flush();
        System.out.println("\n ===================================== After Flush ===================================== \n");

        tx.commit();
    }













    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public OneToManyTest() {
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
