package org.example.idMappingStrategy;

import javax.persistence.*;

/**
 * 1. 우선,
 * <property name="hibernate.id.new_generator_mappings" value="true"/> 옵션은
 * 의미없어 보인다.
 *
 * 2. 시퀀스는 ddl-auto를 create 해두더라도, entity Mapping과 기존에 DB에 존재하는 sequence가 incrementSize가 다르면
 * 시퀀스를 새로 생성하지 않는다. 즉, create가 제대로 작동하지 않는다. 내가 직접 시퀀스를 지우면 create는 해준다.
 *
 * 3. 시퀀스를 처음 매핑할 때, allocationSize는 incrementSize와 매핑된다.
 */

@Entity
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "SOMESEQUENCE",
        initialValue = 1,
        allocationSize = 50
)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "BOARD_SEQ_GENERATOR")
    private Long id;

    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public String getName(){
        return name;
    }
}
