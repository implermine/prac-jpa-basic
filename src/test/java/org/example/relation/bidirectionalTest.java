package org.example.relation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * 양방향 매핑시 가장 많이 하는 실수들
 */
public class bidirectionalTest {

    /**
     * 원래 Database를 고려해보면, Foreign Key를 관리하는 Member쪽에만 데이터를 넣어주면 끝이지만,
     * 객체 관계를 고려하면 양쪽에 넣어주는게 맞다.
     */
    @Test
    @DisplayName("연관관계의 주인에 값을 입력해야 한다. (순수한 객체 관계를 고려하면 항상 양쪽 다 값을 입력해야 한다.)")
    void test1(){
        // 저장
        Team team = new Team();
        team.setId(1L);
        team.setName("TeamA");
        em.persist(team);


        Member member = new Member();
        member.setId(1L);
        member.setUsername("member1");
        member.setTeam(team);
        em.persist(member);

        // 역방향(주인이 아닌 방향)만 연관관계 설정 (외래키 null)
//        team.getMembers().add(member); // 어차피 읽기 전용임 따라서 여긴 의미없음
        // 그러나 읽기전용을 읽기전용처럼 쓰지 않으려면 고민을 해봐야한다.(양방향 객체지양 지원 메소드)


//        em.flush();
//        em.clear();


        Team findTeam = em.find(Team.class, 1L);
        List<Member> members = team.getMembers();
        for (Member eachMember : members) {
            System.out.println("eachMember.getUserName() = " + eachMember.getUsername());
        }

        /**
         * findTeam은 foreignKey로 DB상에선 연결되어 있지만,
         * 영속성 컨텍스트에선 연결되어있지 않다.
         * 따라서, iterate를 하여도 등록된 Member가 없다.
         *
         */


    }






    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public bidirectionalTest() {
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
