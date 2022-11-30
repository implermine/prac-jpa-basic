package org.example.cascade.model.notCascaded;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Setter
public class NotCascadedParent {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public NotCascadedParent(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "parent")
    private List<NotCascadedChild> childList = new ArrayList<>();


    public void removeChild(NotCascadedChild child) {
        this.childList.remove(child);
    }

    /**
     * 연관관계 편의 메서드
     */
    public void addChildBoth(NotCascadedChild child) {
        if (child == null) {
            return;
        }

        child.setParentBoth(this);
    }


    // accessor:default
    void addChild(NotCascadedChild child) {
        if (child == null) {
            return;
        }

        this.childList.add(child);
    }
}
