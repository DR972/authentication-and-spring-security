package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CERTIFICATE_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CREATE_CUSTOMER_ORDER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CUSTOMER_ORDER_CONTROLLER;
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

@Component("customerOrderHateoasAdder")
public class CustomerOrderHateoasAdder implements HateoasAdder<CustomerOrderDto> {

    @Override
    public void addLinks(CustomerOrderDto customerOrderDto) {
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(customerOrderDto.getOrderId())).withRel(GET_CUSTOMER_ORDER_BY_ID));
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList("5", "1")).withRel(GET_CUSTOMER_ORDER_LIST));
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).createCustomerOrder(new CustomerOrderDto())).withRel(CREATE_CUSTOMER_ORDER));
        customerOrderDto.getGiftCertificates().forEach(c -> {
            c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(c.getCertificateId())).withRel(GET_GIFT_CERTIFICATE_BY_ID));
            c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(t.getId())).withRel(GET_TAG_BY_ID)));
        });
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<CustomerOrderDto> customerOrders, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int numberPages = (int) Math.ceil((float) customerOrders.getTotalNumberObjects() / rows);

        addSimpleResourceLinks(customerOrders, pageNumber, rows, numberPages);
        addLinksToResourcesListPages(customerOrders, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<CustomerOrderDto> customerOrders, int pageNumber, int rows, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(customerOrders.getResources().get(0).getCustomerId()))).withRel(GET_CUSTOMER_ORDER_BY_ID));
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(pageNumber), String.valueOf(rows))).withRel(GET_CUSTOMER_ORDER_LIST));
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).createCustomerOrder(new CustomerOrderDto())).withRel(CREATE_CUSTOMER_ORDER));

            customerOrders.getResources().forEach(o -> {
                o.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(o.getOrderId()))).withRel(GET_CUSTOMER_ORDER_BY_ID));
                o.getGiftCertificates().forEach(c -> {
                    c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel(GET_GIFT_CERTIFICATE_BY_ID));
                    c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel(GET_TAG_BY_ID)));
                });
            });
        }
    }

    private void addLinksToResourcesListPages(ResourceDto<CustomerOrderDto> customerOrders, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList("1", String.valueOf(rows))).withRel(GET_CUSTOMER_ORDER_LIST + PAGE_1));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(pageNumber - 1), String.valueOf(rows)))
                        .withRel(GET_CUSTOMER_ORDER_LIST + PREVIOUS_PAGE + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(pageNumber + 1), String.valueOf(rows)))
                        .withRel(GET_CUSTOMER_ORDER_LIST + NEXT_PAGE + (pageNumber + 1)));
            }
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(numberPages), String.valueOf(rows)))
                    .withRel(GET_CUSTOMER_ORDER_LIST + LAST_PAGE + numberPages));
        }
    }
}
