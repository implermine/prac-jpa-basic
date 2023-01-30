package org.example.customDomain.InsertWithoutParent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Setter
@Getter
public class Child_insertWithoutParent {

    @Id
    private Long childId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "PARENT_ID", referencedColumnName = "PARENT_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT), nullable = true
            ,insertable = false,updatable = false
    )
    private Parent_inserWithoutParent parent;

    @Column(name = "PARENT_ID")
    private Long parentId;
}
