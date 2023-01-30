package org.example.customDomain.InsertWithoutParent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Parent_inserWithoutParent {

    @Id
    @Column(name = "PARENT_ID")
    private Long parentId;

    private String name;

}
