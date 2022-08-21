package org.example.relation.manytomany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member_Product_ManyToMany { // 보통 이게 ORDER라는 이름이 되겠죠

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member_ManyToMany member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product_ManyToMany product;
}
