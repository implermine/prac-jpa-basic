package org.example.inheritance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class InheritanceTest {

    @Test
    @DisplayName("@Inheritance(strategy = InheritanceType.JOINED) 의 경우에 어떤식으로 INSERT가 되고 어떤식으로 SELECT가 되는지")
    void test(){

        Movie movie = new Movie();
        movie.setDirector("aaaa");
        movie.setActor("bbbb");
        movie.setName("바람과함께사라지다");
        movie.setPrice(10000);

        em.persist(movie);

        System.out.println("\n\n\n\n");
        System.out.println("두번의 INSERT 쿼리로 나뉘어져서 슈퍼타입과 서브타입에 각각 데이터를 밀어넣는것을 예상\n\n");
        em.flush();
        System.out.println("\n\n\n\n");

        em.clear();

        System.out.println("\n\n\n\n");
        System.out.println("Movie.PFK와 Item.PK를 조인해서 가져오는것으로 예상 \n\n");
        Movie movie1 = em.find(Movie.class, 1L);
        System.out.println("\n\n\n\n");

    }




    private final EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction tx;

    public InheritanceTest() {
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
