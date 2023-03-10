package org.ttn.ecommerce.auditing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;

import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable<U> {

  @JsonIgnore
  @CreatedBy
  protected U createdBy;

  @JsonIgnore
  @CreatedDate
  @Temporal(TIMESTAMP)
  protected Date createdDate;

  @JsonIgnore
  @LastModifiedBy
  protected U lastModifiedBy;

  @JsonIgnore
  @LastModifiedDate
  @Temporal(TIMESTAMP)
  protected Date lastModifiedDate;
}