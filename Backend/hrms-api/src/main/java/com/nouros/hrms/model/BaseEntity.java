package com.nouros.hrms.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;


/**
 The class com.enttribe.btc.model.BaseEntity is a JPA (Java Persistence API) entity, a class that can be persisted to a relational database.
This class is the parent class for all other entities in the application. It contains common fields such as the user who created the entity, the time the entity was created, the user who last modified the entity, and the time the entity was last modified.
This class also has two additional fields, creator and lastModifier of type User and represents the user who created the entity and the user who last modified the entity respectively.
The Getter, Setter is the lombok library. These annotations generates the getter and setter methods for this class.
This class uses JPA's Auditing feature, using @EntityListeners(AuditingEntityListener.class) annotation, which will automatically set the createdBy, createdDate, lastModifiedBy, and lastModifiedDate values when the entity is persisted or updated.
MappedSuperclass is a JPA annotation indicating that this class will be used as the base class for other JPA entities. This means that the fields of this class will be inherited by other entities, but this class itself is not an entity and can't be used in a JPA query.
ManyToOne and JoinColumn is JPA annotation that establishes a many-to-one association between entities. The fetch attribute is used to specify how the entities are loaded. Setting it to FetchType.LAZY means that the associated entities will not be loaded from the database until they are first accessed.
*/



/**
 * ManyToOne - This annotation is used to indicate that this field is a many-to-one relationship. In this case, it is indicating that this entity is related to another entity "User" via a many-to-one relationship.
JoinColumn - This annotation is used to specify the column that is used to join this entity with the other entity in the many-to-one relationship. In this case, it is the "CREATOR" column.
CreatedBy - This annotation is used to indicate that this field will be populated with the user that created this entity.
Column - This annotation is used to specify the column in the table where this field will be stored. In this case, it is the "CREATED_TIME" column.
CreatedDate - This annotation is used to indicate that this field will be populated with the date when this entity was created.
LastModifiedBy - This annotation is used to indicate that this field will be populated with the user that last modified this entity.
LastModifiedDate - This annotation is used to indicate that this field will be populated with the date when this entity was last modified.
*/

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "CREATOR", updatable = false)
  @CreatedBy
  private User creator;

  @Column(name = "CREATED_TIME", insertable = true, updatable = false)
  @CreatedDate
  private Date createdTime;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "LAST_MODIFIER")
  @LastModifiedBy
  private User lastModifier;

  @Column(name = "MODIFIED_TIME", insertable = true, updatable = true)
  @LastModifiedDate
  private Date modifiedTime;


}

