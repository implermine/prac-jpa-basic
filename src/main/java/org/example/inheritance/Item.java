package org.example.inheritance;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 슈퍼타입
 */
@Inheritance(strategy = InheritanceType.JOINED) // InheritanceType.TABLE_PER_CLASS 로 해두면 이 클래스가 Entity 화 되지 않도록 이 클래스를 abstract로 해주는게 좋다.
// 그렇다고 @Entity를 떼버리면 공유된 필드의 어노테이션을 못받아가는것 같다, Movie에서 @Id가 없다고 해버리네
@DiscriminatorColumn // DB 레벨에서 DTYPE을 DEFAULT로 컬럼을 넣어준다
@Setter
@Getter
@Entity

/**
 * 가장 좋은건 SINGLE_TABLE이다 SELECT와 INSERT가 1개씩인데 // 요거랑
 * JOIN은 INSERT가 2번, SELECT는 조인 // 요거중에 고민
 * TABLE_PER_CLASS는 INSERT는 1번, SELECT가 상위 abstract class로 조회했을 때, UNION을 해서 모든 테이블을 뒤져야 한다.
 */
public class Item { // public abstract class Item_ when table_per_class

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

}
