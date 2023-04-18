package org.example.relation.onetoone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book_OneToOne {

    @Id
    private Long id;

    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY)
    private Manuscript_OneToOne manuscript;

}
