package org.example.Idmappingstrategy;

import org.example.BaseCondition;
import org.example.idMappingStrategy.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class SequenceTest extends BaseCondition {

    @Test
    @DisplayName("시퀀스 잘 만들어지나 테스트")
    void test(){
        Board board = new Board();
        Board board2 = new Board();
        Board board3 = new Board();




        System.out.println("=== Before Persist Board1");
        // 여긴 inititation 이라 한번 부르고 // 1
        em.persist(board);
        System.out.println("=== After Persist Board1");

        System.out.println("===Before Persist board2");
        // 여기선 실제로 allocationSize(50)만큼 밀어야되서 부르고 // 2~51
        em.persist(board2);
        System.out.println("===After Persist board2");

        System.out.println("===Before Persist board3");
        // 여긴 안부름
        em.persist(board3);
        System.out.println("===After Persist board3");


        System.out.println("board.getId() = " + board.getId());
        System.out.println("board2.getId() = " + board2.getId());
        System.out.println("board3.getId() = " + board3.getId());




        System.out.println("===Before Flush");
        em.flush();
        em.clear();
        tx.commit();
        em.close();
        System.out.println("===After Flush");

        super.setUp();

        Board board4 = new Board();
        System.out.println("===Before Persist board4");
        em.persist(board4); //여기서 시퀀스 값 52을 할당하는것을 목표로 함, 쉽지않음. was 내의 어딘가에 Sequence가 저장되어 있는데 쉽게 지워지지가 않네.
        // 그래서 이건 그냥 4임.
        // 그냥 DB에서 확인했을 때, SELECT NEXT VALUE FOR SOMESEQUENCE;가 101인것을 보면, 51까지 사용하고 ,52에서+49 되어(NEXT) 101인 것을 확인 할 수 있다.
        System.out.println("===After Persist board4");

        System.out.println("board4.getId() = " + board4.getId());



    }
}
