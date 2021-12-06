package com.swecor.aerotek.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public abstract class Auditing{

    @JsonIgnore
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;
    @JsonIgnore
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @JsonIgnore
    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;
    @JsonIgnore
    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}
