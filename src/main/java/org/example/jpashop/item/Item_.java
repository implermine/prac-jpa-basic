package org.example.jpashop.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpashop.category.Category_;
import org.example.jpashop.mappedsuperclass.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class Item_ extends BaseEntity { // 싱글테이블 전략이더라도 슈퍼타입을 abstract로 할 수 있다. 왜냐면 구체타입이 없는경우가 없다고 판단 될 경우. 따라서 Join 방식도 마찬가지

    @Id @GeneratedValue
    @Column(name="ITEM_ID")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category_> categories = new ArrayList<>();
}
