package org.example.inheritance;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 서브타입 1
 */
@Entity
@DiscriminatorValue(value = "DALBUM") // 슈퍼타입 DTYPE컬럼에 들어가게 될 VALUE STRING
public class Album extends Item {

    private String artist;

}
