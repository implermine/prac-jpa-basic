package org.example.jpashop.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpashop.delivery.Delivery_;
import org.example.jpashop.mappedsuperclass.BaseEntity;
import org.example.jpashop.member.Member_;
import org.example.jpashop.orderitem.OrderItem_;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ORDERS")
@NoArgsConstructor
@Getter
@Setter
public class Order_ extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

//    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID" ,referencedColumnName = "MEMBER_ID")
    private Member_ member;

    private LocalDate orderDate;

    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderItem_> orderItems = new ArrayList<>();

    // 완전한 연관관계 편의메서드가 아니다
    public void addOrderItem(OrderItem_ orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    @OneToOne
    @JoinColumn(name= "DELIVERY_ID")
    private Delivery_ delivery;


}
