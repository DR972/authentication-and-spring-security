package com.epam.esm.hateoas.impl;

import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CERTIFICATE_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CUSTOMER_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.CUSTOMER_ORDER_CONTROLLER;
import static com.epam.esm.hateoas.impl.util.RequestParameterProvider.TAG_CONTROLLER;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class {@code CustomerOrderHateoasAdderToCustomer} is implementation of interface {@link HateoasAdder}
 * and intended to work with {@link CustomerOrderDto} objects.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@Component("customerOrderHateoasAdderToCustomer")
public class CustomerOrderHateoasAdderToCustomer implements HateoasAdder<CustomerOrderDto> {

    @Override
    public void addLinks(CustomerOrderDto customerOrderDto) {
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderByCustomerIdAndOrderId(String.valueOf(customerOrderDto.getCustomerId()),
                String.valueOf(customerOrderDto.getOrderId()))).withRel("getCustomerOrderByCustomerIdAndOrderId"));
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(customerOrderDto.getCustomerId()))).withRel("getCustomerById"));
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList("5", "1")).withRel("getCustomerList"));
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerOrderDto.getCustomerId()), "5", "1")).withRel("getCustomerOrderList"));

        customerOrderDto.getGiftCertificates().forEach(c -> {
            c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel("getCertificateById"));
            c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
        });
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<CustomerOrderDto> customerOrders, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int customerId = params[2];
        int numberPages = (int) Math.ceil((float) customerOrders.getTotalNumberObjects() / rows);
        customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerId), String.valueOf(pageNumber), String.valueOf(rows)))
                .withRel("getCustomerOrderList"));

        addSimpleResourceLinks(customerOrders, customerId, pageNumber, rows, numberPages);
        addLinksToResourcesListPages(customerOrders, customerId, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<CustomerOrderDto> customerOrders, int customerId, int pageNumber, int rows, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(customerId))).withRel("getCustomerById"));
            customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(pageNumber), String.valueOf(rows))).withRel("getCustomerList"));
            customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderByCustomerIdAndOrderId(String.valueOf(customerId),
                    customerOrders.getResources().get(0).getOrderId())).withRel("getCustomerOrderByCustomerIdAndOrderId"));

            customerOrders.getResources().forEach(o -> {
                o.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(o.getOrderId()))).withRel("getCustomerOrderById"));
                o.getGiftCertificates().forEach(c -> {
                    c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel("getCertificateById"));
                    c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
                });
            });
        }
    }

    private void addLinksToResourcesListPages(ResourceDto<CustomerOrderDto> customerOrders, int customerId, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerId), "1", String.valueOf(rows)))
                    .withRel("getCustomerOrderList page 1"));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerId), String.valueOf(pageNumber - 1), String.valueOf(rows)))
                        .withRel("getCustomerOrderList previous page " + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerId), String.valueOf(pageNumber + 1), String.valueOf(rows)))
                        .withRel("getCustomerOrderList next page " + (pageNumber + 1)));
            }
            customerOrders.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerId), String.valueOf(numberPages), String.valueOf(rows)))
                    .withRel("getCustomerOrderList last page " + numberPages));
        }
    }
}
