package org.example.relation.onetoone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member_OneToOne {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID", unique = true)
    private Locker_OneToOne locker;
}
