package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.CustomerController;
import com.epam.esm.controller.CustomerOrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.CustomerDto;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class {@code CustomerHateoasAdder} is implementation of interface {@link HateoasAdder}
 * and intended to work with {@link CustomerDto} objects.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@Component
public class CustomerHateoasAdder implements HateoasAdder<CustomerDto> {
    private static final Class<CustomerController> CUSTOMER_CONTROLLER = CustomerController.class;
    private static final Class<CustomerOrderController> CUSTOMER_ORDER_CONTROLLER = CustomerOrderController.class;
    private static final Class<CertificateController> CERTIFICATE_CONTROLLER = CertificateController.class;
    private static final Class<TagController> TAG_CONTROLLER = TagController.class;

    @Override
    public void addLinks(CustomerDto customerDto) {
        customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(customerDto.getCustomerId()))).withRel("getCustomerById"));
        customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList("5", "1")).withRel("getCustomerList"));

        if (!customerDto.getCustomerOrders().isEmpty()) {
            customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderByCustomerIdAndOrderId(String.valueOf(customerDto.getCustomerId()),
                    String.valueOf(customerDto.getCustomerOrders().get(0).getOrderId()))).withRel("getCustomerOrderByCustomerIdAndOrderId"));
        }
        customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderList(String.valueOf(customerDto.getCustomerId()), "5", "1")).withRel("getCustomerOrderList"));
        customerDto.add(linkTo(methodOn(CUSTOMER_CONTROLLER).createCustomerOrder(String.valueOf(customerDto.getCustomerId()), new CustomerOrderDto())).withRel("createCustomerOrder"));

        customerDto.getCustomerOrders().forEach(o -> {
            o.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(o.getOrderId()))).withRel("getCustomerOrderById"));
            o.getGiftCertificates().forEach(c -> {
                c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel("getCertificateById"));
                c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
            });
        });
    }

    @Override
    public void addLinksToEntitiesList(ResourceDto<CustomerDto> customers, int... params) {
        int rows = params[0];
        int pageNumber = params[1];
        int numberPages = (int) Math.ceil((float) customers.getTotalNumberObjects() / rows);
        customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(pageNumber), String.valueOf(rows))).withRel("getCustomerList"));

        addSimpleResourceLinks(customers, pageNumber, numberPages);
        addLinksToResourcesListPages(customers, pageNumber, rows, numberPages);
    }

    private void addSimpleResourceLinks(ResourceDto<CustomerDto> customers, int pageNumber, int numberPages) {
        if (pageNumber < (numberPages + 1)) {
            customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(customers.getResources().get(0).getCustomerId()))).withRel("getCustomerById"));
            if (!customers.getResources().get(0).getCustomerOrders().isEmpty()) {
                customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerOrderByCustomerIdAndOrderId(String.valueOf(customers.getResources().get(0).getCustomerId()),
                        String.valueOf(customers.getResources().get(0).getCustomerOrders().get(0).getOrderId()))).withRel("getCustomerOrderByCustomerIdAndOrderId"));
            }
            customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).createCustomerOrder(String.valueOf(customers.getResources().get(0).getCustomerId()), new CustomerOrderDto()))
                    .withRel("createCustomerOrder"));
            customers.getResources().forEach(c -> {
                c.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerById(String.valueOf(c.getCustomerId()))).withRel("getCustomerById"));
                c.getCustomerOrders().forEach(o -> {
                    o.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(o.getOrderId()))).withRel("getCustomerOrderById"));
                    o.getGiftCertificates().forEach(g -> {
                        g.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(g.getCertificateId()))).withRel("getCertificateById"));
                        g.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
                    });
                });
            });
        }
    }

    private void addLinksToResourcesListPages(ResourceDto<CustomerDto> customers, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList("1", String.valueOf(rows))).withRel("getCustomerList page 1"));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(pageNumber - 1), String.valueOf(rows)))
                        .withRel("getCustomerList previous page " + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(pageNumber + 1), String.valueOf(rows)))
                        .withRel("getCustomerList next page " + (pageNumber + 1)));
            }
            customers.add(linkTo(methodOn(CUSTOMER_CONTROLLER).getCustomerList(String.valueOf(numberPages), String.valueOf(rows))).withRel("getCustomerList last page " + numberPages));
        }
    }
}