package org.example.customDomain.mapsIdWithEmbedded.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "PERSON")
@Entity
@ToString
public class Person {

    @Id
    @Column(name = "PERSON_ID")
    private Long personId;
    private String name;
    private int height;
}
