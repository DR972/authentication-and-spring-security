package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The class {@code BaseEntity} represents the base class for all entities.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<I> extends RepresentationModel<BaseEntity<I>> implements Serializable {
    /**
     * BaseEntity id.
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected I id;

    /**
     * String operation.
     */
    @Column(name = "operation")
    protected String operation;

    /**
     * LocalDateTime timestamp.
     */
    @Column(name = "timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    protected LocalDateTime timestamp;


    /**
     * The constructor creates a BaseEntity object
     *
     * @param id I id
     */
    protected BaseEntity(I id) {
    }

    /**
     * The method is called before creating an entity in the database.
     */
    @PrePersist
    public void onPrePersist() {
        audit("INSERT");
    }

    /**
     * The method is called before saving entity changes to the database.
     */
    @PreUpdate
    public void onPreUpdate() {
        audit("UPDATE");
    }

    /**
     * The method is called before deleting an entity in the database.
     */
    @PreRemove
    public void onPreRemove() {
        audit("DELETE");
    }

    /**
     * The method performs an audit of database changes.
     *
     * @param operation String operation
     */
    private void audit(String operation) {
        setOperation(operation);
        setTimestamp(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getOperation() != null ? getOperation().hashCode() : 0);
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        return result;
    }
}
