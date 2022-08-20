package org.example.jpashop.orderitem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpashop.item.Item;
import org.example.jpashop.order.Order;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

//    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;
//    private Long itemId;
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    private int orderPrice;

    private int count;

}
