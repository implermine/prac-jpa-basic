package org.example.multiple_bag_fetch_exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class SoccerTeam {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "soccerTeam")
    private List<SoccerTeamSong> soccerTeamSongs;

    @OneToMany(mappedBy = "soccerTeam")
    private List<SoccerMember> soccerMembers;

    public SoccerTeam(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
