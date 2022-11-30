package org.example.cascade.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Parent(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(mappedBy = "parent")
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
