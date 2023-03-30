package org.example.multiple_bag_fetch_exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BaseBallTeam {
    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "baseBallTeam")
    private List<BaseBallTeamSong> baseBallTeamSongs;

    @OneToMany(mappedBy = "baseBallTeam")
    @OrderColumn(name = "arrangement_index")
    private List<BaseBallMember> baseBallMembers;

    public BaseBallTeam(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
