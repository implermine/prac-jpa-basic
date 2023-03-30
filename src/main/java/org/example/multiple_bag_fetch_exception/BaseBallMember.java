package org.example.multiple_bag_fetch_exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BaseBallMember {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private BaseBallTeam baseBallTeam;

    @Column(name = "arrangement_index", nullable = false)
    private int arrangementIndex;

    public BaseBallMember(Long id, String name, int arrangementIndex) {
        this.id = id;
        this.name = name;
        this.arrangementIndex = arrangementIndex;
    }
}
