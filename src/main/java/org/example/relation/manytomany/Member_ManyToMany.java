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
public class Member_ManyToMany {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT_MAPPING", // <- JOINTABLE 물리적 명칭
            joinColumns = @JoinColumn(name = "MEMBER_ID", referencedColumnName = "ID"), // FK 명칭 (저쪽 PK)
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID") // 저쪽 FK 명칭 (이쪽 PK)
    ) // @JoinTable이라 함은, Member도 Product도 연관관계의 주인이 아니다.
    // 엔티티로 승격이 더 낫다고 한다. 왜냐면 언제 추가했는지 등 추가 정보가 들어갈 수 있기 때문
    private List<Product_ManyToMany> productList = new ArrayList<>();

    /**
     * 엔티티 승격에 대해 알아보자
     */
//    @OneToMany(mappedBy = "member")
//    private List<Member_Product_ManyToMany> memberProductList = new ArrayList<>();
}
