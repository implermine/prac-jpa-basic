package org.example.inheritance;

import javax.persistence.Entity;

/**
 * μλΈνμ 3
 */
@Entity
public class Book extends Item {
    private String author;
    private String isbn;
}
