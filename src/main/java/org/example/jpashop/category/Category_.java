package org.example.jpashop.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.jpashop.item.Item_;
import org.example.jpashop.mappedsuperclass.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
@NoArgsConstructor
@Getter
public class Category_ extends BaseEntity {

    @Id
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name="CATEGORY_ITEM",
            joinColumns = @JoinColumn(name="CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name="ITEM_ID")
    )
    private List<Item_> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Category_ parent;

    @OneToMany(mappedBy = "parent")
    private List<Category_> child = new ArrayList<>();
}
