package org.example.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Table(name ="CORPORATION")
@Entity
public class Corporation {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "corporation",fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

    public Corporation(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
