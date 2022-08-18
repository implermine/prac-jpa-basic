package org.example.relation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ManyToOneTest {
    @Test
    @DisplayName("ManyToOne 객체 참조했을 때, INSERT 쿼리 잘 나가나")
    void check_two_distinct_insert_query(){

        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(1L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        System.out.println("Before Persist Team");
        // 쿼리 미발생
        em.persist(team);
        System.out.println("After Persist Team");

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        System.out.println("Before Persist Member");
        // 쿼리 미발생
        em.persist(member);
        System.out.println("After Persist Member");

        System.out.println("Before flush");
        // 쿼리 발생
        em.flush();
        System.out.println("After flush");
    }

    @Test
    @DisplayName("ManyToOne 객체 호출했을 때, SELECT 쿼리 조인해서 잘 나가나")
    void check_select_query(){

        //given

        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(1L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        // 쿼리 미발생
        em.persist(team);

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        // 쿼리 미발생
        em.persist(member);

        // 쿼리 발생
        em.flush();
        em.clear();

        System.out.println("Before Find");
        Member foundMember = em.find(Member.class, 1L);
        System.out.println("After Find");
    }

    @Test
    @DisplayName("ManyToOne 객체 호출했을 때, UPDATE 쿼리 잘 나가나")
    void check_update_query(){

        //given

        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(1L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        // 쿼리 미발생
        em.persist(team);

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        // 쿼리 미발생
        em.persist(member);

        // 쿼리 발생
        System.out.println("Before First Flush");
        em.flush();
        System.out.println("After First Flush");


        member.getTeam().setName("modified Team Name");

        System.out.println("Before Second Flush");
        em.flush();
        System.out.println("After Second Flush");

    }

    @Test
    @DisplayName("ManyToOne 객체 호출했을 때, UPDATE 쿼리 잘 나가나, FOREIGN KEY 대상으로")
    void check_update_query_for_foreign_key(){

        //given
        Team savedTeam = new Team();
        savedTeam.setName("SavedTeam");
        savedTeam.setId(1L);

        em.persist(savedTeam);

        System.out.println("/// Expect Insert Query");
        em.flush();
        em.clear();
        System.out.println("///");



        // 부모객체 (연관관계 미주인) 먼저 생성
        Team team = new Team();
        team.setName("TeamA");
        team.setId(2L);

        // 부모 저장 (CASCADE가 없으므로, 따로 영속화(Persist) 해줘야 한다.
        // 쿼리 미발생
        em.persist(team);

        // 자식 생성 (연관관계 주인)
        Member member = new Member();
        member.setUsername("memberA");
        member.setId(1L);
        member.setTeam(team); // 단방향 연관관계 설정, 객체 참조 저장

        // 쿼리 미발생
        em.persist(member);

        // 쿼리 발생
        System.out.println("Before First Flush");
        System.out.println("/// Expect 2 insert query");
        em.flush();
        System.out.println("///");
        System.out.println("After First Flush");


        member.setTeam(savedTeam); // 팀을 기존팀으로 바꾼거지

        System.out.println("Before Second Flush");
        System.out.println("/// Expect 1 update query");
        // 그러나 2개의 쿼리가 나간다, savedTeam을 조회하는 SELECT 쿼리가 발생한 후에 UPDATE 쿼리가 발생하는데,
        // 이는 부모 데이터가 존재하는지 여부를 확인해, 데이터베이스 무결성을 유지하기 위함으로 보인다.
        em.flush();
        System.out.println("///");
        System.out.println("After Second Flush");

        tx.commit();

    }





























    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public ManyToOneTest() {
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
