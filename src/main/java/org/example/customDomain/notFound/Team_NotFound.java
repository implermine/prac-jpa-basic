package org.example.customDomain.notFound;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
public class Team_NotFound implements Serializable {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<Member_NotFound> members = new ArrayList<>();

    public Team_NotFound(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addMember(Member_NotFound member){
        member.changeTeam(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Member_NotFound> getMembers() {
        return members;
    }
}
