package org.example.multiple_bag_fetch_exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "VEHICLE")
public class Vehicle {

    @Id
    private Long id;

    private String name;

    private String type;

    @OneToMany(mappedBy = "vehicle")
    private List<Order> ordersList = new ArrayList<>();
}
