package com.example.account.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
/* BaseEntitiy
@EntityListeners(AuditingEntityListener.class) */
public class AccountUser extends BaseEntity {
    /* BaseEntitiy
    @Id
    @GeneratedValue
    private Long id;
    */

    private String name;

    /* BaseEntitiy
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    */
}
