package org.example.customDomain.mapsIdWithEmbedded.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name ="PERSON_DETAIL")
@Entity
public class PersonDetail {
    @EmbeddedId
    private CompositeId personDetailId;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class CompositeId implements Serializable {

        @Column(name = "PERSON_ID")
        private Long personId;
        private String part;

        public static CompositeId of(Long personId, String part){
            return new CompositeId(personId,part);
        }
    }

    private int weight;

    @MapsId("personId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", referencedColumnName = "PERSON_ID")
    private Person person;

}
