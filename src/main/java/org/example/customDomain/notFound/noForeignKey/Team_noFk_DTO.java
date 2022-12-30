package org.example.customDomain.notFound.noForeignKey;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter
public class Team_noFk_DTO {
    private Long id;
    private String name;

    public Team_noFk_DTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
