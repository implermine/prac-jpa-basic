package org.example.embed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
@Getter
@Embeddable
@ToString
@NoArgsConstructor
public class Period {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isWork(){
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(this.startDate) && now.isBefore(this.endDate);
    }
}
