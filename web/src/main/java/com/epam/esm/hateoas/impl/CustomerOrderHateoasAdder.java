package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.CustomerOrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component("customerOrderHateoasAdder")
public class CustomerOrderHateoasAdder implements HateoasAdder<CustomerOrderDto> {
    private static final Class<CustomerOrderController> CUSTOMER_ORDER_CONTROLLER = CustomerOrderController.class;
    private static final Class<CertificateController> CERTIFICATE_CONTROLLER = CertificateController.class;
    private static final Class<TagController> TAG_CONTROLLER = TagController.class;

    @Override
    public void addLinks(CustomerOrderDto customerOrderDto) {
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(customerOrderDto.getOrderId())).withRel("getCustomerOrderById"));
        customerOrderDto.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList("5", "1")).withRel("getCustomerOrderList"));

        customerOrderDto.getGiftCertificates().forEach(c -> {
            c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(c.getCertificateId())).withRel("getCertificateById"));
            c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(t.getId())).withRel("getTagById")));
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
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(customerOrders.getResources().get(0).getCustomerId()))).withRel("getCustomerOrderById"));
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(pageNumber), String.valueOf(rows))).withRel("getCustomerOrderList"));

            customerOrders.getResources().forEach(o -> {
                o.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderById(String.valueOf(o.getOrderId()))).withRel("getCustomerOrderById"));
                o.getGiftCertificates().forEach(c -> {
                    c.add(linkTo(methodOn(CERTIFICATE_CONTROLLER).getCertificateById(String.valueOf(c.getCertificateId()))).withRel("getCertificateById"));
                    c.getTags().forEach(t -> t.add(linkTo(methodOn(TAG_CONTROLLER).getTagById(String.valueOf(t.getId()))).withRel("getTagById")));
                });
            });
        }
    }

    private void addLinksToResourcesListPages(ResourceDto<CustomerOrderDto> customerOrders, int pageNumber, int rows, int numberPages) {
        if (numberPages > 1) {
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList("1", String.valueOf(rows))).withRel("getCustomerOrderList page 1"));
            if (pageNumber > 2 && pageNumber < (numberPages + 1)) {
                customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(pageNumber - 1), String.valueOf(rows)))
                        .withRel("getCustomerOrderList previous page " + (pageNumber - 1)));
            }
            if (pageNumber < (numberPages - 1)) {
                customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(pageNumber + 1), String.valueOf(rows)))
                        .withRel("getCustomerOrderList next page " + (pageNumber + 1)));
            }
            customerOrders.add(linkTo(methodOn(CUSTOMER_ORDER_CONTROLLER).getCustomerOrderList(String.valueOf(numberPages), String.valueOf(rows)))
                    .withRel("getCustomerOrderList last page " + numberPages));
        }
    }
}
