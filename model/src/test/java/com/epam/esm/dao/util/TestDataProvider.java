package com.epam.esm.dao.util;

import com.epam.esm.entity.Customer;
import com.epam.esm.entity.CustomerOrder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The class {@code SqlQuery} stores data for tests.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public final class TestDataProvider {

    public static final String REST = "rest";
    public static final String NEW = "new";
    public static final Tag TAG_1 = new Tag(1, "rest");
    public static final Tag TAG_2 = new Tag(2, "nature");
    public static final Tag TAG_3 = new Tag(3, "shopping");
    public static final Tag NEW_TAG = new Tag(0, NEW);
    public static final Tag TAG_4 = new Tag(4, "atv");
    public static final Tag TAG_5 = new Tag(5, "horse");
    public static final Tag TAG_6 = new Tag(6, "theater");
    public static final Tag TAG_7 = new Tag(7, "tool");
    public static final Tag TAG_8 = new Tag(8, "food");
    public static final Tag TAG_9 = new Tag(9, "supermarket");
    public static final Tag TAG_10 = new Tag(10, "restaurant");
    public static final Tag TAG_11 = new Tag(11, "flight");
    public static final Tag TAG_12 = new Tag(12, "visit");
    public static final Tag NEW_TAG_5 = new Tag(5, "new horse");

    public static final String NAME = "name";
    public static final String SCRIPT = "script";
    public static final String LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String HORSE = "horse";
    public static final String RIDING = "riding";
    public static final String VISIT = "visit";

    public static final GiftCertificate GIFT_CERTIFICATE_1 = new GiftCertificate(1, "ATV riding",
            "Description ATV riding", new BigDecimal("100.00"), 10, LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-07T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(4, "atv")));

    public static final GiftCertificate GIFT_CERTIFICATE_2 = new GiftCertificate(2, "Horse riding",
            "Horse riding description", new BigDecimal("80.00"), 8, LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-05T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(5, "horse")));

    public static final GiftCertificate GIFT_CERTIFICATE_3 = new GiftCertificate(3, "Visiting a restaurant",
            "Visiting the Plaza restaurant", new BigDecimal("50.00"), 7, LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-02T14:15:13.257"), Arrays.asList(new Tag(8, "food"), new Tag(10, "restaurant"), new Tag(12, "visit")));

    public static final GiftCertificate GIFT_CERTIFICATE_4 = new GiftCertificate(4, "Visit to the drama theater",
            "Description visit to the drama theater", new BigDecimal("45.00"), 2, LocalDateTime.parse("2022-03-30T10:12:45.123"),
            LocalDateTime.parse("2022-04-08T14:15:13.257"), Arrays.asList(new Tag(6, "theater"), new Tag(12, "visit")));

    public static final GiftCertificate GIFT_CERTIFICATE_5 = new GiftCertificate(5, "Shopping at the tool store",
            "Description shopping at the tool store", new BigDecimal("30.00"), 10, LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-04-01T14:15:13.257"), Arrays.asList(new Tag(3, "shopping"), new Tag(7, "tool")));

    public static final GiftCertificate GIFT_CERTIFICATE_6 = new GiftCertificate(6, "Shopping at the supermarket",
            "Shopping at Lidl supermarket chain", new BigDecimal("80.00"), 12, LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-14T14:15:13.257"), Arrays.asList(new Tag(6, "shopping"), new Tag(8, "food"), new Tag(9, "supermarket")));

    public static final GiftCertificate GIFT_CERTIFICATE_7 = new GiftCertificate(7, "Hot air balloon flight",
            "An unforgettable hot air balloon flight", new BigDecimal("150.00"), 12, LocalDateTime.parse("2022-03-01T10:12:45.123"),
            LocalDateTime.parse("2022-03-14T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(11, "flight")));

    public static final GiftCertificate NEW_GIFT_CERTIFICATE = new GiftCertificate("new GiftCertificate", "new description",
            new BigDecimal("10.00"), 10, Arrays.asList(new Tag("rest"), new Tag("nature"), new Tag("supermarket")));

    public static final GiftCertificate NEW_GIFT_CERTIFICATE_WITH_NEW_TAG = new GiftCertificate("new GiftCertificate", "new description",
            new BigDecimal("10.00"), 10, Arrays.asList(new Tag("rest"), new Tag("nature"), new Tag("new")));

    public static final GiftCertificate UPDATE_GIFT_CERTIFICATE_1 = new GiftCertificate(5, "Shopping", "new Description",
            new BigDecimal("15.00"), 20, null, null,
            Arrays.asList(new Tag(3, "shopping"), new Tag(7, "tool"), new Tag(13, "new")));

    public static final GiftCertificate UPDATE_GIFT_CERTIFICATE_2 = new GiftCertificate(6, "new Shopping", null,
            new BigDecimal("15.00"), 0, null, null,
            Arrays.asList(new Tag(3, "shopping"), new Tag(8, "food"), new Tag(9, "supermarket")));

    public static final CustomerOrder CUSTOMER_ORDER_1 = new CustomerOrder(1, 1L, LocalDateTime.parse("2022-04-02T10:12"),
            Collections.singletonList(GIFT_CERTIFICATE_7), new BigDecimal("150.00"));
    public static final CustomerOrder CUSTOMER_ORDER_2 = new CustomerOrder(2, 2L, LocalDateTime.parse("2022-04-03T10:12"),
            Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2), new BigDecimal("180.00"));
    public static final CustomerOrder CUSTOMER_ORDER_3 = new CustomerOrder(3, 3L, LocalDateTime.parse("2022-04-05T10:12"),
            Arrays.asList(GIFT_CERTIFICATE_2, GIFT_CERTIFICATE_4), new BigDecimal("125.00"));
    public static final CustomerOrder CUSTOMER_ORDER_4 = new CustomerOrder(4, 1L, LocalDateTime.parse("2022-04-04T10:12"),
            Arrays.asList(GIFT_CERTIFICATE_5, GIFT_CERTIFICATE_6), new BigDecimal("110.00"));
    public static final CustomerOrder CUSTOMER_ORDER_5 = new CustomerOrder(5, 2L, LocalDateTime.parse("2022-04-07T10:12"),
            Collections.singletonList(GIFT_CERTIFICATE_3), new BigDecimal("50.00"));

    public static final Customer CUSTOMER_1 = new Customer(1, "user1", Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_4));
    public static final Customer CUSTOMER_2 = new Customer(2, "user2", Arrays.asList(CUSTOMER_ORDER_2, CUSTOMER_ORDER_5));
    public static final Customer CUSTOMER_3 = new Customer(3, "user3", Collections.singletonList(CUSTOMER_ORDER_3));
    public static final Customer NEW_CUSTOMER = new Customer(0, NEW, new ArrayList<>());

    /**
     * Private constructor without parameters.
     * Restrict instantiation of the class.
     */
    private TestDataProvider() {
    }
}
