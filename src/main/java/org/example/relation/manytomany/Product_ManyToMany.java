package org.example.relation.manytomany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Product_ManyToMany {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @ManyToMany(mappedBy = "productList")
    private List<Member_ManyToMany> memberList = new ArrayList<>();

//    @OneToMany(mappedBy = "product")
//    private List<Member_Product_ManyToMany> productMemberList = new ArrayList<>();
}
