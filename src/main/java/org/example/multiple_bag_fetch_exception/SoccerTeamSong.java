package org.example.multiple_bag_fetch_exception;

        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;

        import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class SoccerTeamSong {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private SoccerTeam soccerTeam;

    public SoccerTeamSong(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
