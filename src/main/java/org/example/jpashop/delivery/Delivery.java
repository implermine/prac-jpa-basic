package org.example.jpashop.delivery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.jpashop.order.Order;

import javax.persistence.*;

@Entity
@Table(name="DELIVERY")
@NoArgsConstructor
@Getter
public class Delivery {

    @Id
    @Column(name = "DELIVERY_ID")
    private Long id;

    private String city;

    private String street;

    private String zipCode;

    private DeliveryStatus deliveryStatus;

    @OneToOne(mappedBy = "delivery")
    private Order order;

}
