package org.example.cascade.model.cascaded;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Parent(String name) {
        this.name = name;
    }

    //    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL) // 원형
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private List<Child> childList = new ArrayList<>();


    public void removeChild(Child child) {
        this.childList.remove(child);
    }

    /**
     * 연관관계 편의 메서드
     */
    public void addChildBoth(Child child) {
        if (child == null) {
            return;
        }

        child.setParentBoth(this);
    }


    // accessor:default
    void addChild(Child child) {
        if (child == null) {
            return;
        }

        this.childList.add(child);
    }
}
