package org.example.inheritance;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * 서브타입 2
 */
@Entity
@NoArgsConstructor
@Setter
@Getter
public class Movie extends Item {

    private String director;
    private String actor;
}
