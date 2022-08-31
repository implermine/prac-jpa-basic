package org.example.jpashop.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("MOVIE")
public class Movie_ extends Item_{
    private String director;
    private String actor;
}
