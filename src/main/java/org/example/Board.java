package org.example;

import javax.persistence.*;

/**
 * 시퀀스 만드려고 만드는 예제 (책 p.136 참조)
 *
 * CREATE TABLE BOARD(
 * ID BIGINT NOT NULL PRIMARY KEY,
 * DATA VARCHAR(255)
 * )
 *
 * 시퀀스 생성
 *
 * CREATE SEQUENCE BOARD_SEQ START WITH 1 INCREMENT BY 1;
 *
 *
 * 이미 존재하는 시퀀스를 사용하는 방법은...?
 */

@Entity
//@SequenceGenerator(
//        name = "BOARD_SEQ_GENERATOR",
//        sequenceName = "BOARD_SEQ", // 매핑할 데이터베이스 시퀀스 이름
//        initialValue = 1, allocationSize = 1
//)
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "MYSEQUENCE",
        allocationSize = 7 //<- 실제 시퀀스를 그대로 쓰고 싶으면 이렇게, 이거 늘리면 persist 할 때마다 쿼리 안함.
)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "BOARD_SEQ_GENERATOR")
    private Long id;

    public Long getId() {
        return id;
    }
}
