package com.epam.esm.hateoas.impl.util;

import com.epam.esm.controller.AuthenticationController;
import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.CustomerController;
import com.epam.esm.controller.CustomerOrderController;
import com.epam.esm.controller.TagController;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * The class {@code RequestParameterProvider} stores request parameter for hateoas.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
public final class RequestParameterProvider {
    public static final Class<AuthenticationController> AUTHENTICATION_CONTROLLER = AuthenticationController.class;
    public static final Class<CertificateController> CERTIFICATE_CONTROLLER = CertificateController.class;
    public static final Class<TagController> TAG_CONTROLLER = TagController.class;
    public static final Class<CustomerController> CUSTOMER_CONTROLLER = CustomerController.class;
    public static final Class<CustomerOrderController> CUSTOMER_ORDER_CONTROLLER = CustomerOrderController.class;

    public static final String NAME = "name";
    public static final String TAG = "tag";
    public static final String SORTING = "sorting";
    public static final String DESCRIPTION = "description";
    public static final MultiValueMap<String, String> allRequestParams = new LinkedMultiValueMap<>();

    public static final String GET_TAG_BY_ID = "getTagById";
    public static final String GET_TAG_LIST = "getTagList";
    public static final String GET_GIFT_CERTIFICATE_BY_ID = "getGiftCertificateById";
    public static final String GET_GIFT_CERTIFICATE_LIST = "getGiftCertificateList";
    public static final String GET_MOST_POPULAR_TAGS = "getMostWidelyUsedTagsOfCustomersWithHighestCostOfAllOrders";
    public static final String GET_CUSTOMER_BY_ID = "getCustomerById";
    public static final String GET_CUSTOMER_LIST = "getCustomerList";
    public static final String GET_CUSTOMER_ORDER_BY_ID = "getCustomerOrderById";
    public static final String GET_CUSTOMER_ORDER_LIST = "getCustomerOrderList";
    public static final String CREATE_CUSTOMER_ORDER = "createCustomerOrder";
    public static final String GET_CUSTOMER_ORDER_BY_CUSTOMER_ID_AND_ORDER_ID = "getCustomerOrderByCustomerIdAndOrderId";
    public static final String PAGE_1 = " page 1";
    public static final String PREVIOUS_PAGE = " previous page ";
    public static final String NEXT_PAGE = " next page ";
    public static final String LAST_PAGE = " last page ";

    static {
        allRequestParams.add(SORTING, "lastUpdateDate");
        allRequestParams.add(SORTING, "name");
        allRequestParams.add(SORTING, "id");
        allRequestParams.add(SORTING, "price");
        allRequestParams.add(TAG, "lobortis est");
        allRequestParams.add(TAG, "hendrerit at");
        allRequestParams.add(NAME, "suspend");
        allRequestParams.add(NAME, "ligula");
        allRequestParams.add(DESCRIPTION, "ipsum");
        allRequestParams.add(DESCRIPTION, "primis");
    }

    /**
     * Private constructor without parameters.
     * Restrict instantiation of the class.
     */
    private RequestParameterProvider() {
    }
}
