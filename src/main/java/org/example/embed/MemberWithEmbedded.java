package org.example.embed;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
    private Address address;

    @Embedded
    private Contact contact;


}
