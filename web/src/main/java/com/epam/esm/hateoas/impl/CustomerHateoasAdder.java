package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.CustomerDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CERTIFICATE_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CUSTOMER_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CUSTOMER_ORDER_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_CUSTOMER_BY_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_CUSTOMER_LIST;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_CUSTOMER_ORDER_BY_CUSTOMER_ID_AND_ORDER_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_CUSTOMER_ORDER_BY_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_CUSTOMER_ORDER_LIST;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_GIFT_CERTIFICATE_BY_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.GET_TAG_BY_ID;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.LAST_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.NEXT_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.PAGE_1;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.PREVIOUS_PAGE;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.TAG_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class {@code CustomerHateoasAdder} is implementation of interface {@link HateoasAdder}
 * and intended to work with {@link CustomerDto} objects.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@Component("customerHateoasAdder")
public class CustomerHateoasAdder implements HateoasAdder<CustomerDto> {

    @Override
    public void addLinks(CustomerDto customerDto) {
        customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(customerDto.getCustomerId()))).withRel(GET_CUSTOMER_BY_ID));
        customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList("5", "1")).withRel(GET_CUSTOMER_LIST));

        if (!customerDto.getCustomerOrders().isEmpty()) {
            customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderByCustomerIdAndOrderId(String.valueOf(customerDto.getCustomerId()),
                    String.valueOf(customerDto.getCustomerOrders().get(0).getOrderId()))).withRel(GET_CUSTOMER_ORDER_BY_CUSTOMER_ID_AND_ORDER_ID));
        }
        customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerDto.getCustomerId()), "5", "1")).withRel(GET_CUSTOMER_ORDER_LIST));

        customerDto.getCustomerOrders().forEach(o -> {
            o.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(o.getOrderId()))).withRel(GET_CUSTOMER_ORDER_BY_ID));
            o.getGiftCertificates().forEach(c -> {
                c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel(GET_GIFT_CERTIFICATE_BY_ID));
                c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel(GET_TAG_BY_ID)));
            });
        });
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<CustomerDto> customers, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int numberPages = (int) Math.ceil((float) customers.getTotalNumberObjects() / rows);
        customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(pageNumber), String.valueOf(rows))).withRel(GET_CUSTOMER_LIST));

        addSimpleResourceLinks(customers, pageNumber, numberPages);
        addLinksToResourcesListPages(customers, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<CustomerDto> customers, int pageNumber, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(customers.getResources().get(0).getCustomerId()))).withRel(GET_CUSTOMER_BY_ID));
            if (!customers.getResources().get(0).getCustomerOrders().isEmpty()) {
                customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderByCustomerIdAndOrderId(String.valueOf(customers.getResources().get(0).getCustomerId()),
                        String.valueOf(customers.getResources().get(0).getCustomerOrders().get(0).getOrderId()))).withRel(GET_CUSTOMER_ORDER_BY_CUSTOMER_ID_AND_ORDER_ID));
            }

            customers.getResources().forEach(c -> {
                c.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(c.getCustomerId()))).withRel(GET_CUSTOMER_BY_ID));
                c.getCustomerOrders().forEach(o -> {
                    o.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(o.getOrderId()))).withRel(GET_CUSTOMER_ORDER_BY_ID));
                    o.getGiftCertificates().forEach(g -> {
                        g.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(g.getCertificateId()))).withRel(GET_GIFT_CERTIFICATE_BY_ID));
                        g.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel(GET_TAG_BY_ID)));
                    });
                });
            });
        }
    }

    private void addLinksToResourcesListPages(ResourceDto<CustomerDto> customers, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList("1", String.valueOf(rows))).withRel(GET_CUSTOMER_LIST + PAGE_1));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(pageNumber - 1), String.valueOf(rows)))
                        .withRel(GET_CUSTOMER_LIST + PREVIOUS_PAGE + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(pageNumber + 1), String.valueOf(rows)))
                        .withRel(GET_CUSTOMER_LIST + NEXT_PAGE + (pageNumber + 1)));
            }
            customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(numberPages), String.valueOf(rows))).withRel(GET_CUSTOMER_LIST + LAST_PAGE + numberPages));
        }
    }
}