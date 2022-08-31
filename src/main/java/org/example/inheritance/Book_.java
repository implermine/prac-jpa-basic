package org.example.inheritance;

import javax.persistence.Entity;

/**
 * 서브타입 3
 */
@Entity
public class Book_ extends Item_ {
    private String author;
    private String isbn;
}
