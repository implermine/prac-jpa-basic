package org.example.relation.onetoone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Manuscript_OneToOne {

    @Id
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_book")
    private Book_OneToOne book;

}
