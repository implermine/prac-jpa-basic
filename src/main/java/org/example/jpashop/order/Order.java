package org.example.jpashop.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpashop.delivery.Delivery;
import org.example.jpashop.mymember.MyMember;
import org.example.jpashop.orderitem.OrderItem;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDERS")
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

//    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID" ,referencedColumnName = "MEMBER_ID")
    private MyMember myMember;

    private LocalDate orderDate;

    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    // 완전한 연관관계 편의메서드가 아니다
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    @OneToOne
    @JoinColumn(name= "DELIVERY_ID")
    private Delivery delivery;


}
