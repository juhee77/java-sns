package com.lahee.mutsasns.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at", updatable = false)
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "deleted_at", updatable = false)
    private Instant deletedAt;

    @CreatedBy
    private String createdUser;

    @LastModifiedBy
    private String modifiedUser;

    @Override
    public String toString() {
        return "BaseEntity{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", createdUser='" + createdUser + '\'' +
                ", modifiedUser='" + modifiedUser + '\'' +
                '}';
    }
}
