package org.example.cascade.model.notCascaded;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class NotCascadedChild {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public NotCascadedChild(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private NotCascadedParent parent;


    /**
     * 부모가 정해져있다면, change
     * 그렇지 않다면 save
     * <p>
     * 연관관계 편의 메서드
     */
    public void setParentBoth(NotCascadedParent parent) {

        // 연관관계에 null을 대입하여 연관관계를 끊어내고 싶은 경우라면
        if (parent == null) {
            this.parent.removeChild(this);
            this.parent = null;
            return;
        }

        // 부모가 정해져있지 않은 상황이라면
        if (this.parent == null) {
            this.parent = parent;
            this.parent.addChild(this);
            return;
        }

        // 부모가 정해져있는 상황이라면
        this.parent.removeChild(this);
        this.parent = parent;
        this.parent.addChild(this);
    }
}
