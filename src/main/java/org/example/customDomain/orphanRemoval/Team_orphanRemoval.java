package org.example.customDomain.orphanRemoval;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Team_orphanRemoval {

    @Id
    private Long id;

    private String name;

    public Team_orphanRemoval(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //CASCADE.PERSIST 없이 orphanRemoval 불가
    @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST ,orphanRemoval = true)
    private List<Member_orphanRemoval> memberList = new ArrayList<>();

    // 파라미터로 받은 member와 equal한 첫번째 element를 memberList에서 삭제
    public boolean removeFirst(Member_orphanRemoval member){

        boolean isRemoved = false;

        Iterator<Member_orphanRemoval> iter = this.memberList.iterator();
        while(iter.hasNext()){
            Member_orphanRemoval next = iter.next();
            if(next.equals(member)){
                iter.remove();
                isRemoved = true;
                break;
            }
        }

        return isRemoved;
    }
}
