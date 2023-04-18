package org.example.relation.onetoone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class CorpDetail {

    @Id
    @Column(name = "CORP_ID")
    private Long corpId;

    private String detailName;

    //@OneToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "CORP_ID" , referencedColumnName = "CORP_ID")
    //private Corp corp;
}
