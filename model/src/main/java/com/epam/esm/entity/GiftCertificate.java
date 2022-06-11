package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

/**
 * The class {@code GiftCertificate} represents GiftCertificate entity.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "gift_certificate")
@Audited
public class GiftCertificate extends BaseEntity<Long> {
    /**
     * GiftCertificate name.
     */
    private String name;
    /**
     * GiftCertificate description.
     */
    private String description;
    /**
     * GiftCertificate price.
     */
    private BigDecimal price;
    /**
     * GiftCertificate duration.
     */
    private Integer duration;
    /**
     * GiftCertificate createDate.
     */
    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    /**
     * GiftCertificate lastUpdateDate.
     */
    @Column(name = "last_update_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;

    /**
     * List<Tag> tags
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "gift_certificate_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    /**
     * List<CustomerOrder> customerOrders
     */
    @ManyToMany(mappedBy = "giftCertificates")
    private List<CustomerOrder> customerOrders;

    /**
     * The constructor creates a GiftCertificate object
     *
     * @param id             long id
     * @param name           String name
     * @param description    String description
     * @param price          BigDecimal price
     * @param duration       int duration
     * @param createDate     LocalDateTime createDate
     * @param lastUpdateDate LocalDateTime lastUpdateDate
     * @param tags           List<Tag> tags
     */
    public GiftCertificate(long id, String name, String description, BigDecimal price, int duration, LocalDateTime createDate,
                           LocalDateTime lastUpdateDate, List<Tag> tags) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    /**
     * The constructor creates a GiftCertificate object
     *
     * @param name           String name
     * @param description    String description
     * @param price          BigDecimal price
     * @param duration       int duration
     * @param createDate     LocalDateTime createDate
     * @param lastUpdateDate LocalDateTime lastUpdateDate
     * @param tags           List<Tag> tags
     */

    public GiftCertificate(String name, String description, BigDecimal price, int duration, LocalDateTime createDate,
                           LocalDateTime lastUpdateDate, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    /**
     * The constructor creates a GiftCertificate object
     *
     * @param name        String name
     * @param description String description
     * @param price       BigDecimal price
     * @param duration    int duration
     * @param tags        List<Tag> tags
     */
    public GiftCertificate(String name, String description, BigDecimal price, Integer duration, List<Tag> tags) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.tags = tags;
    }

    /**
     * The constructor creates a GiftCertificate object
     *
     * @param id long id
     */

    public GiftCertificate(long id) {
        super(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GiftCertificate)) {
            return false;
        }

        GiftCertificate giftCertificate = (GiftCertificate) o;

        if (!name.equals(giftCertificate.name)) {
            return false;
        }
        if (!description.equals(giftCertificate.description)) {
            return false;
        }
        if (!price.equals(giftCertificate.price)) {
            return false;
        }
        if (!duration.equals(giftCertificate.duration)) {
            return false;
        }
        if (!createDate.equals(giftCertificate.createDate)) {
            return false;
        }
        return lastUpdateDate.equals(giftCertificate.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getDuration() != null ? getDuration().hashCode() : 0);
        result = 31 * result + (getCreateDate() != null ? getCreateDate().hashCode() : 0);
        result = 31 * result + (getLastUpdateDate() != null ? getLastUpdateDate().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        result = 31 * result + (getCustomerOrders() != null ? getCustomerOrders().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GiftCertificate.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("price=" + price)
                .add("duration=" + duration)
                .add("createDate=" + createDate)
                .add("lastUpdateDate=" + lastUpdateDate)
                .add("tags=" + tags)
                .toString();
    }
}
