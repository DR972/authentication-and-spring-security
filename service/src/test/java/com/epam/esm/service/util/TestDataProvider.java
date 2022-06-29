package com.epam.esm.service.util;

import com.epam.esm.dto.CustomerDto;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Customer;
import com.epam.esm.entity.CustomerOrder;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

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
    public static final TagDto TAG_DTO_1 = new TagDto("1", "rest");
    public static final TagDto TAG_DTO_2 = new TagDto("2", "nature");
    public static final TagDto TAG_DTO_3 = new TagDto("3", "shopping");
    public static final TagDto NEW_DTO_TAG = new TagDto("0", NEW);

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String TAG = "tag";
    public static final String SORTING = "sorting";
    public static final String LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String LAST_UPDATE_DATE_SNAKE_CASE = "last_update_date";
    public static final String HORSE = " horse";
    public static final String RIDING = "riding";
    public static final String VISIT = "visit";
    public static final LocalDateTime DATE_TIME = LocalDateTime.parse("2022-05-01T00:00:00.001");

    public static final GiftCertificate GIFT_CERTIFICATE_1 = new GiftCertificate(1, "ATV riding",
            "Description ATV riding", new BigDecimal("100"), 10, LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-07T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(4, "atv")));

    public static final GiftCertificate GIFT_CERTIFICATE_2 = new GiftCertificate(2, "Horse riding",
            "Horse riding description", new BigDecimal("80"), 8, LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-05T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(5, "horse")));

    public static final GiftCertificate GIFT_CERTIFICATE_3 = new GiftCertificate(3, "Visiting a restaurant",
            "Visiting the Plaza restaurant", new BigDecimal("50"), 7, LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-02T14:15:13.257"), Arrays.asList(new Tag(8, "food"), new Tag(10, "restaurant"), new Tag(12, "visit")));

    public static final GiftCertificate GIFT_CERTIFICATE_4 = new GiftCertificate(4, "Visit to the drama theater",
            "Description visit to the drama theater", new BigDecimal("45"), 2, LocalDateTime.parse("2022-03-30T10:12:45.123"),
            LocalDateTime.parse("2022-04-08T14:15:13.257"), Arrays.asList(new Tag(6, "theater"), new Tag(12, "visit")));

    public static final GiftCertificate GIFT_CERTIFICATE_5 = new GiftCertificate(5, "Shopping at the tool store",
            "Description shopping at the tool store", new BigDecimal("30"), 10, LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-04-01T14:15:13.257"), Arrays.asList(new Tag(3, "shopping"), new Tag(7, "tool")));

    public static final GiftCertificate GIFT_CERTIFICATE_6 = new GiftCertificate(6, "Shopping at the supermarket",
            "Shopping at Lidl supermarket chain", new BigDecimal("80"), 12, LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-14T14:15:13.257"), Arrays.asList(new Tag(6, "shopping"), new Tag(8, "food"), new Tag(9, "supermarket")));

    public static final GiftCertificate GIFT_CERTIFICATE_7 = new GiftCertificate(7, "Hot air balloon flight",
            "An unforgettable hot air balloon flight", new BigDecimal("150"), 12, LocalDateTime.parse("2022-03-01T10:12:45.123"),
            LocalDateTime.parse("2022-03-14T14:15:13.257"), Arrays.asList(new Tag(1, "rest"), new Tag(2, "nature"), new Tag(11, "flight")));

    public static final GiftCertificate GIFT_CERTIFICATE_8 = new GiftCertificate("new GiftCertificate",
            "new description", new BigDecimal("10"), 10, LocalDateTime.parse("2022-05-01T00:00:00.001"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new Tag("rest"), new Tag("nature"), new Tag("new")));

    public static final GiftCertificate GIFT_CERTIFICATE_9 = new GiftCertificate(5, null,
            "Description shopping at the tool store", null, 10, null,
            null, Arrays.asList(new Tag("shopping"), new Tag("tool"), new Tag("new")));

    public static final GiftCertificate GIFT_CERTIFICATE_5_NEW = new GiftCertificate(5, "Shopping at the tool store",
            "Description shopping at the tool store", new BigDecimal("30"), 10, LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new Tag(3, "shopping"), new Tag(7, "tool"), new Tag("new")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_1 = new GiftCertificateDto("1", "ATV riding",
            "Description ATV riding", "100", "10", LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-07T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("4", "atv")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_2 = new GiftCertificateDto("2", "Horse riding",
            "Horse riding description", "80", "8", LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-05T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("5", "horse")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_3 = new GiftCertificateDto("3", "Visiting a restaurant",
            "Visiting the Plaza restaurant", "50", "7", LocalDateTime.parse("2022-04-02T10:12:45.123"),
            LocalDateTime.parse("2022-04-02T14:15:13.257"), Arrays.asList(new TagDto("8", "food"), new TagDto("10", "restaurant"), new TagDto("12", "visit")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_4 = new GiftCertificateDto("4", "Visit to the drama theater",
            "Description visit to the drama theater", "45", "2", LocalDateTime.parse("2022-03-30T10:12:45.123"),
            LocalDateTime.parse("2022-04-08T14:15:13.257"), Arrays.asList(new TagDto("6", "theater"), new TagDto("12", "visit")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_5 = new GiftCertificateDto("5", "Shopping at the tool store",
            "Description shopping at the tool store", "30", "10", LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-04-01T14:15:13.257"), Arrays.asList(new TagDto("3", "shopping"), new TagDto("7", "tool")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_6 = new GiftCertificateDto("6", "Shopping at the supermarket",
            "Shopping at Lidl supermarket chain", "80", "12", LocalDateTime.parse("2022-04-01T10:12:45.123"),
            LocalDateTime.parse("2022-04-14T14:15:13.257"), Arrays.asList(new TagDto("6", "shopping"), new TagDto("8", "food"), new TagDto("9", "supermarket")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_7 = new GiftCertificateDto("7", "Hot air balloon flight",
            "An unforgettable hot air balloon flight", "150", "12", LocalDateTime.parse("2022-03-01T10:12:45.123"),
            LocalDateTime.parse("2022-03-14T14:15:13.257"), Arrays.asList(new TagDto("1", "rest"), new TagDto("2", "nature"), new TagDto("11", "flight")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_8 = new GiftCertificateDto("new GiftCertificate",
            "new description", "10", "10", LocalDateTime.parse("2022-05-01T00:00:00.001"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new TagDto("rest"), new TagDto("nature"), new TagDto("new")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_9 = new GiftCertificateDto(null,
            "Description shopping at the tool store", null, "10", null,
            null, Arrays.asList(new TagDto("shopping"), new TagDto("tool"), new TagDto("new")));

    public static final GiftCertificateDto GIFT_CERTIFICATE_DTO_5_NEW = new GiftCertificateDto("5", "Shopping at the tool store",
            "Description shopping at the tool store", "30", "10", LocalDateTime.parse("2022-03-25T10:12:45.123"),
            LocalDateTime.parse("2022-05-01T00:00:00.001"), Arrays.asList(new TagDto("3", "shopping"), new TagDto("7", "tool"), new TagDto("15", "new")));

    public static final String CUSTOMER_EMAIL_1 = "wse@wss.com";

    public static final CustomerOrder CUSTOMER_ORDER_1 = new CustomerOrder(1L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("80"));
    public static final CustomerOrder CUSTOMER_ORDER_2 = new CustomerOrder(2L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("180"));
    public static final CustomerOrder CUSTOMER_ORDER_3 = new CustomerOrder(3L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("100"));
    public static final CustomerOrder CUSTOMER_ORDER_5 = new CustomerOrder(5L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("120"));
    public static final CustomerOrder CUSTOMER_ORDER_6 = new CustomerOrder(6L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("60"));
    public static final CustomerOrder CUSTOMER_ORDER_7 = new CustomerOrder(7L, LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("40"));

    public static final Customer CUSTOMER_1 = new Customer("Customer1", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_1, CUSTOMER_ORDER_5));
    public static final Customer CUSTOMER_2 = new Customer("Customer2", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_2, CUSTOMER_ORDER_6));
    public static final Customer CUSTOMER_3 = new Customer("Customer3", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_3, CUSTOMER_ORDER_7));
    public static final Customer NEW_CUSTOMER = new Customer(0, NEW, new ArrayList<>());

    public static final CustomerOrderDto CUSTOMER_ORDER_DTO_1 = new CustomerOrderDto("1", "1", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("80"));
    public static final CustomerOrderDto CUSTOMER_ORDER_DTO_2 = new CustomerOrderDto("2", "2", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("180"));
    public static final CustomerOrderDto CUSTOMER_ORDER_DTO_3 = new CustomerOrderDto("3", "3", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("100"));
    public static final CustomerOrderDto CUSTOMER_ORDER_DTO_5 = new CustomerOrderDto("5", "1", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("120"));
    public static final CustomerOrderDto CUSTOMER_ORDER_DTO_6 = new CustomerOrderDto("6", "2", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("60"));
    public static final CustomerOrderDto CUSTOMER_ORDER_DTO_7 = new CustomerOrderDto("7", "3", LocalDateTime.parse("2022-05-01T00:00:00.001"), new ArrayList<>(), new BigDecimal("40"));

    public static final CustomerDto CUSTOMER_DTO_1 = new CustomerDto("1", "Customer1", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_DTO_1, CUSTOMER_ORDER_DTO_5));
    public static final CustomerDto CUSTOMER_DTO_2 = new CustomerDto("2", "Customer2", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_DTO_2, CUSTOMER_ORDER_DTO_6));
    public static final CustomerDto CUSTOMER_DTO_3 = new CustomerDto("3", "Customer3", "qwed", "wse@wss.com", Role.ADMIN, Arrays.asList(CUSTOMER_ORDER_DTO_3, CUSTOMER_ORDER_DTO_7));
    public static final CustomerDto NEW_DTO_CUSTOMER = new CustomerDto("0", NEW, "qwed", "bvfgtre@wss.com", Role.USER, new ArrayList<>());

    public static final CustomerOrder NEW_CUSTOMER_ORDER = new CustomerOrder(0, 1L, LocalDateTime.parse("2022-05-01T00:00:00.001"),
            Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2), new BigDecimal("180"));

    public static final CustomerOrderDto NEW_DTO_CUSTOMER_ORDER = new CustomerOrderDto("0", "2", null, Arrays.asList(GIFT_CERTIFICATE_DTO_1, GIFT_CERTIFICATE_DTO_2), null);

    /**
     * Private constructor without parameters.
     * Restrict instantiation of the class.
     */
    private TestDataProvider() {
    }
}
