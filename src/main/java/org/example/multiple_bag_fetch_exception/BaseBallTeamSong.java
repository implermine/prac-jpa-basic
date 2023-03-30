package org.example.multiple_bag_fetch_exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BaseBallTeamSong {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private BaseBallTeam baseBallTeam;

    public BaseBallTeamSong(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
