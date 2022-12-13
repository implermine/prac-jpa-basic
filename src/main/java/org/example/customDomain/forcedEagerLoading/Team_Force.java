package org.example.customDomain.forcedEagerLoading;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
public class Team_Force implements Serializable {

    @Id
    private Long id;

    private String name;

    @Column(name ="SURROGATED_MEMBER_FK")
    private Long surrogated_member_fk;

    public Team_Force(Long id, String name, Long surrogated_member_fk) {
        this.id = id;
        this.name = name;
        this.surrogated_member_fk = surrogated_member_fk;
    }
}
