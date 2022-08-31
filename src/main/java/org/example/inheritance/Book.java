package org.example.inheritance;

import javax.persistence.Entity;

/**
 * 서브타입 3
 */
@Entity
public class Book extends Item {
    private String author;
    private String isbn;
}
