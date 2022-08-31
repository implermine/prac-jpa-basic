package org.example.jpashop.orderitem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpashop.item.Item_;
import org.example.jpashop.mappedsuperclass.BaseEntity;
import org.example.jpashop.order.Order_;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class OrderItem_ extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

//    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order_ order;
//    private Long itemId;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item_ item;

    private int orderPrice;

    private int count;

}
