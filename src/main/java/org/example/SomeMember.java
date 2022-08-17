package org.example;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name = "MEMBER")
@Getter
@Setter
public class SomeMember {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

//    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate createdDate; // JAVA 8 부터 Date + Temporal 대신 LocalDate를 사용가능

    // Date , Time , TimeStamp (Date + Time) 이다.
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    @Transient // 매핑하고 싶지 않음
    private int temp;
}
