package org.example.jpashop.mappedsuperclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity { // 그냥 class로 해도되지만 어차피 생성할 일이 없으므로 추상클래스로 사용하는게 낫다.
    private String createBy;
    private String lastModifiedBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
