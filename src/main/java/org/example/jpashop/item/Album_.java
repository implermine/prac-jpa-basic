package org.example.jpashop.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("ALBUM")
public class Album_ extends Item_{
    private String artist;
    private String etc;

}
