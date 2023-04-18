package org.example.multiple_bag_fetch_exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class OrderDetail {

    @Id
    private Long id;

    private String name;

    private Long price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;


}
