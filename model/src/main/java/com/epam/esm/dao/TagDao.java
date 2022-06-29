package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface {@code GiftCertificateDao} describes abstract behavior for working in database.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Repository
public interface TagDao extends Dao<Tag, Long> {

    /**
     * The method finds objects Tag in the table 'Tag' by name.
     *
     * @param name String name
     * @return Optional<Tag> object
     */
    Optional<Tag> findTagByName(String name);

    /**
     * The method finds list the most widely used tags Of Customers with the highest cost of all orders in the table `Tag`.
     *
     * @param pageable Pageable pageable
     * @return Page<Tag>
     */
    @Query(nativeQuery = true, value = "WITH customer_sum_orders AS " +
            "(SELECT customer_id AS id, SUM (amount) AS total FROM  customer_order GROUP BY customer_id), " +
            "customer_highest_cost_orders AS " +
            "(SELECT id FROM customer_sum_orders WHERE total >= (SELECT MAX(total) FROM customer_sum_orders)), " +
            "popular_tag AS " +
            "(SELECT tag.id, tag.name, tag.operation, tag.timestamp, COUNT(tag.id) AS quantity FROM customer_order " +
            "LEFT JOIN customer_order_gift_certificate ON customer_order.id = customer_order_id " +
            "LEFT JOIN gift_certificate ON customer_order_gift_certificate.gift_certificate_id = gift_certificate.id " +
            "LEFT JOIN gift_certificate_tag ON gift_certificate_tag.gift_certificate_id = gift_certificate.id LEFT JOIN tag ON tag_id = tag.id " +
            "WHERE customer_id IN (SELECT id FROM customer_highest_cost_orders) GROUP BY tag.id) " +
            "SELECT id, name, operation, timestamp FROM popular_tag WHERE quantity >= (SELECT MAX(quantity) FROM popular_tag)")
    Page<Tag> findMostPopularTag(Pageable pageable);

    /**
     * The method finds count number of rows in the list of the most popular tags.
     *
     * @return count number of rows objects
     */
    @Query(nativeQuery = true, value = "WITH customer_sum_orders AS " +
            "(SELECT customer_id AS id, SUM (amount) AS total FROM  customer_order GROUP BY customer_id), " +
            "customer_highest_cost_orders AS " +
            "(SELECT id FROM customer_sum_orders WHERE total >= (SELECT MAX(total) FROM customer_sum_orders)), " +
            "popular_tag AS " +
            "(SELECT tag.id, tag.name, tag.operation, tag.timestamp, COUNT(tag.id) AS quantity FROM customer_order " +
            "LEFT JOIN customer_order_gift_certificate ON customer_order.id = customer_order_id " +
            "LEFT JOIN gift_certificate ON customer_order_gift_certificate.gift_certificate_id = gift_certificate.id " +
            "LEFT JOIN gift_certificate_tag ON gift_certificate_tag.gift_certificate_id = gift_certificate.id LEFT JOIN tag ON tag_id = tag.id " +
            "WHERE customer_id IN (SELECT id FROM customer_highest_cost_orders) GROUP BY tag.id) " +
            "SELECT COUNT(id) FROM popular_tag WHERE quantity >= (SELECT MAX(quantity) FROM popular_tag)")
    long countMostPopularTag();


    /**
     * The method removes tags that are not associated with certificates in the `Tag` table.
     *
     * @param tags String tags
     */
    @Modifying
    @Query(value = "DELETE FROM tag WHERE tag.name IN (SELECT tag.name FROM unnest(string_to_array(:tags, ',')) u " +
            "LEFT JOIN tag ON tag.name=u LEFT JOIN gift_certificate_tag ON tag.id = tag_id WHERE tag_id is NULL)",
            nativeQuery = true)
    void deleteTagNotAssociatedWithCertificates(String tags);
}
