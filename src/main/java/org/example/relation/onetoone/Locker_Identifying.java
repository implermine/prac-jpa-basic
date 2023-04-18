package org.example.relation.onetoone;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Locker_Identifying {

    @Id
    @Column(name="ID")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name="ID",referencedColumnName = "ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),insertable = false,updatable = false)
    private Member_Identifying member;

    public String getName() {
        System.out.println("through mine");
        return name;
    }

    public Member_Identifying getMember() {
        System.out.println("through getMember");
        return member;
    }
}
