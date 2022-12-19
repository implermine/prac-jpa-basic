package org.example.embed;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class MemberWithEmbedded {

    @Id
    private Long id;

    private String name;

    //일하는 기간 (그러나 Period Type으로 방학기간과 타입이 같음)
    @Embedded
    private Period workPeriod;

    // 방학 기간 (그러나 Period Type으로 일하는 기간과 타입이 같음)
    // 또한, 같은 타입에 다른 DB 컬럼명을 매핑해야 되는 상황임.
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "VACATION_START_DATE")),
            @AttributeOverride(name = "endDate", column = @Column(name = "VACATION_END_DATE"))
    })
    private Period vacationPeriod;

    @Embedded
    private Address homeAddress;

    @ElementCollection // 값 타입 컬렉션으로 선언한다는 의미.
    @CollectionTable // 테이블 지정
            (
                    name = "FAVORITE_FOOD", // 테이블 명
                    joinColumns = @JoinColumn(
                            name = "MEMBER_ID" // 조인 테이블의 PK와 FK일 줄 알았는데 그냥 not null이기만하네
                    )
            )
    @Column(name = "FOOD_NAME") // 예외적으로, Collection이 기본 값 타입일 때, 조인 테이블의 컬럼명을 지정할 수 있다.
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable
            (
                    name = "ADDRESS", // 테이블 명
                    joinColumns = @JoinColumn(
                            //위와 달리, 조인 테이블의 PK와 FK를 분리하고 싶다면?
                            name = "MEMBER_ID", // 조인 테이블의 PK와 FK일 줄 알았는데 그냥 not null이기만하네
                            referencedColumnName = "ID",
                            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT) // 안먹네
                    )
            )
    private List<Address> addressHistories = new ArrayList<>();


    @Embedded
    private Contact contact;


}
