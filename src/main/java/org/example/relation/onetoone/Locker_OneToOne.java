package org.example.relation.onetoone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Locker_OneToOne {


    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToOne(mappedBy = "locker")
    private Member_OneToOne member;
}
