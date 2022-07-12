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
 * The class {@code CustomerOrder} represents CustomerOrder entity.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "customer_order")
@Audited
public class CustomerOrder extends BaseEntity<Long> {
    /**
     * Long customerId
     */
    @Column(name = "customer_id")
    private Long customerId;

    /**
     * LocalDateTime purchaseTime.
     */
    @Column(name = "purchase_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime purchaseTime;

    /**
     * List<GiftCertificate> giftCertificates.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "customer_order_gift_certificate",
            joinColumns = @JoinColumn(name = "customer_order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id")
    )
    private List<GiftCertificate> giftCertificates;

    /**
     * BigDecimal amount.
     */
    private BigDecimal amount;

    /**
     * The constructor creates a CustomerOrder object
     *
     * @param id               long id
     * @param customerId       long customerId
     * @param purchaseTime     LocalDateTime purchaseTime
     * @param giftCertificates ArrayList<GiftCertificate> giftCertificates
     * @param amount           BigDecimal amount
     */
    public CustomerOrder(long id, Long customerId, LocalDateTime purchaseTime, List<GiftCertificate> giftCertificates, BigDecimal amount) {
        super(id);
        this.customerId = customerId;
        this.purchaseTime = purchaseTime;
        this.giftCertificates = giftCertificates;
        this.amount = amount;
    }

    /**
     * The constructor creates a CustomerOrder object
     *
     * @param id long id
     */
    public CustomerOrder(long id) {
        super(id);
    }

    /**
     * The constructor creates a CustomerOrder object
     *
     * @param id               long id
     * @param purchaseTime     LocalDateTime purchaseTime
     * @param giftCertificates ArrayList<GiftCertificate> giftCertificates
     * @param amount           BigDecimal amount
     */
    public CustomerOrder(long id, LocalDateTime purchaseTime, List<GiftCertificate> giftCertificates, BigDecimal amount) {
        super(id);
        this.purchaseTime = purchaseTime;
        this.giftCertificates = giftCertificates;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerOrder)) {
            return false;
        }
        CustomerOrder customerOrder = (CustomerOrder) o;
        if (!purchaseTime.equals(customerOrder.purchaseTime)) {
            return false;
        }
        return amount.equals(customerOrder.amount);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getCustomerId() != null ? getCustomerId().hashCode() : 0);
        result = 31 * result + (getPurchaseTime() != null ? getPurchaseTime().hashCode() : 0);
        result = 31 * result + (getGiftCertificates() != null ? getGiftCertificates().hashCode() : 0);
        result = 31 * result + (getAmount() != null ? getAmount().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomerOrder.class.getSimpleName() + "[", "]")
                .add("customerId=" + customerId)
                .add("purchaseTime=" + purchaseTime)
                .add("giftCertificates=" + giftCertificates)
                .add("amount=" + amount)
                .toString();
    }
}
