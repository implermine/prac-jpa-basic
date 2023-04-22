package org.example.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "EMPLOYEE")
@Entity
public class Employee {

    @Id
    @Column(name ="ID")
    private Integer id;

    @Column(name="NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CORPORATION_ID",referencedColumnName = "ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Corporation corporation;

    public Employee(Integer id, String name, Corporation corporation) {
        this.id = id;
        this.name = name;
        this.corporation = corporation;
    }
}
