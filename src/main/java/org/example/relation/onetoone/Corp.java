package org.example.relation.onetoone;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Corp{

    @Id
    @Column(name = "CORP_ID")
    private Long corpId;

    private String name;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "CORP_ID" , referencedColumnName = "CORP_ID",insertable = false,updatable = false)
    private List<CorpDetail> corpDetail;

}
