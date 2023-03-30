package org.example.multiple_bag_fetch_exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SoccerMember {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private SoccerTeam soccerTeam;

    public SoccerMember(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
