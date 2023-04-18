package org.example.relation.onetoone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member_Identifying {

    @Id
    @Column(name="ID")
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID", referencedColumnName = "ID",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), insertable = false,updatable = false)
    private Locker_Identifying locker;
}
