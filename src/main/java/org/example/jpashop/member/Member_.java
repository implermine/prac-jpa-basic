package org.example.jpashop.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.jpashop.mappedsuperclass.BaseEntity;
import org.example.jpashop.order.Order_;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member_ extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String name;

    private String city;

    private String street;

    private String zipCode;

    /**
     * 도메인이 다르므로 , 양방향보다는 order는 따로 memberId를 가지고 조회하는게 낫다.
     * 즉 , 연관관계를 끊는것이 사실 더 낫다.라고 김영한이 함
     */
    @OneToMany(mappedBy = "member")
    private List<Order_> orders = new ArrayList<>();


    /**
     * @MappedSuperClass
     */
//    private String createBy;
//    private String lastModifiedBy;
//    private LocalDateTime createdAt;
//    private LocalDateTime lastModifiedAt;
}
