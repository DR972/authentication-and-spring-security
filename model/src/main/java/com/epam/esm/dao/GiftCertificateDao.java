package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The interface {@code GiftCertificateDao} describes abstract behavior for working in database.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Repository
@Transactional
public interface GiftCertificateDao extends Dao<GiftCertificate, Long> {

    /**
     * The method finds list GiftCertificate objects in the table `GiftCertificate` by part of the name and description.
     *
     * @param name1        String name
     * @param name2        String name
     * @param description1 String description
     * @param description2 String description
     * @param pageable     Pageable pageable
     * @return Page<GiftCertificate> objects
     */
    Page<GiftCertificate> findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
    (String name1, String name2, String description1, String description2, Pageable pageable);

    /**
     * The method finds count number of rows objects GiftCertificate in the table 'GiftCertificate' by part of the name and description.
     *
     * @param name1        String name
     * @param name2        String name
     * @param description1 String description
     * @param description2 String description
     * @return count number of rows objects
     */
    long countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContaining
    (String name1, String name2, String description1, String description2);

    /**
     * The method finds list GiftCertificate objects in the table `GiftCertificate` by part of the name and description and the tag name.
     *
     * @param name1        String name
     * @param name2        String name
     * @param description1 String description
     * @param description2 String description
     * @param tag          String tagName
     * @param pageable     Pageable pageable
     * @return Page<GiftCertificate> objects
     */
    Page<GiftCertificate> findAllByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
    (String name1, String name2, String description1, String description2, String tag, Pageable pageable);

    /**
     * The method finds count number of rows objects GiftCertificate in the table 'GiftCertificate' by part of the name and description and the tag name.
     *
     * @param name1        String name
     * @param name2        String name
     * @param description1 String description
     * @param description2 String description
     * @param tag          String tagName
     * @return count number of rows objects
     */
    long countByNameIgnoreCaseContainingAndNameIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndDescriptionIgnoreCaseContainingAndTags_Name
    (String name1, String name2, String description1, String description2, String tag);

    /**
     * The method finds list GiftCertificate objects in the table `GiftCertificate` by the part of the name and description and the name of the two tags.
     *
     * @param name1        String name
     * @param name2        String name
     * @param description1 String description
     * @param description2 String description
     * @param tag1         String tagName
     * @param tag2         String tagName
     * @param pageable     Pageable pageable
     * @return Page<GiftCertificate> objects
     */
    @Query(nativeQuery = true, countQuery = "select count(*) from gift_certificate g", value = "WITH gift_certificate_filter AS " +
            "(SELECT g.*, ARRAY_AGG(tag) AS tags FROM gift_certificate g LEFT JOIN gift_certificate_tag ON gift_certificate_id = g.id LEFT JOIN tag ON tag.id = tag_id " +
            "WHERE g.name LIKE CONCAT('%', :name1, '%') AND g.name LIKE CONCAT('%', :name2, '%') " +
            "AND g.description LIKE CONCAT('%', :description1, '%') AND g.description LIKE CONCAT('%', :description2, '%') GROUP BY g.id), " +
            "gift_certificate_filter_tag AS " +
            "(SELECT * FROM gift_certificate_filter WHERE id IN (SELECT gift_certificate_id FROM gift_certificate_tag WHERE tag_id = (SELECT id FROM tag WHERE name = :tag1))) " +
            "SELECT g.* FROM gift_certificate_filter_tag g WHERE g.id IN (SELECT gift_certificate_id FROM gift_certificate_tag WHERE tag_id = (SELECT id FROM tag WHERE name = :tag2)) ")
    Page<GiftCertificate> findAll(String name1, String name2, String description1, String description2, String tag1, String tag2, Pageable pageable);

    /**
     * The method finds count number of rows objects GiftCertificate in the table 'GiftCertificate' by the part of the name and description and the name of the two tags.
     *
     * @param name1        String name
     * @param name2        String name
     * @param description1 String description
     * @param description2 String description
     * @param tag1         String tagName
     * @param tag2         String tagName
     * @return count number of rows objects
     */
    @Query(nativeQuery = true, value = "WITH gift_certificate_filter AS " +
            "(SELECT g.*, ARRAY_AGG(tag) AS tags FROM gift_certificate g LEFT JOIN gift_certificate_tag ON gift_certificate_id = g.id LEFT JOIN tag ON tag.id = tag_id " +
            "WHERE g.name LIKE CONCAT('%', :name1, '%') AND g.name LIKE CONCAT('%', :name2, '%') " +
            "AND g.description LIKE CONCAT('%', :description1, '%') AND g.description LIKE CONCAT('%', :description2, '%') GROUP BY g.id), " +
            "gift_certificate_filter_tag AS " +
            "(SELECT * FROM gift_certificate_filter WHERE id IN (SELECT gift_certificate_id FROM gift_certificate_tag WHERE tag_id = (SELECT id FROM tag WHERE name = :tag1))) " +
            "SELECT COUNT(id) FROM gift_certificate_filter_tag WHERE id IN (SELECT gift_certificate_id FROM gift_certificate_tag WHERE tag_id = (SELECT id FROM tag WHERE name = :tag2))")
    long count(String name1, String name2, String description1, String description2, String tag1, String tag2);

    /**
     * The method finds count number of rows objects GiftCertificate in the table 'GiftCertificate' by tagId.
     *
     * @param id long tagId
     * @return count number of rows objects
     */
    List<GiftCertificate> findAllByTags_Id(long id);
}
