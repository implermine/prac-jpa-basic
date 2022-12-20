package org.example.customDomain.notFound.noForeignKey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Team_noFk {

    @Id
    private Long id;

    private String name;

    public Team_noFk(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
