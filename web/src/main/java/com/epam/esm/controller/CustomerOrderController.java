package com.epam.esm.controller;

import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.CustomerOrderService;
import com.epam.esm.dto.CustomerOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;

/**
 * Class {@code CustomerOrderController} is an endpoint of the API which allows you to perform operations on Customer Orders.
 * Annotated by {@link RestController} without parameters to provide an answer in application/json.
 * Annotated by {@link RequestMapping} with parameter value = "/orders".
 * Annotated by {@link Validated} without parameters  provides checking of constraints in method parameters.
 * So that {@code TagController} is accessed by sending request to /orders.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */

@RestController
@RequestMapping("/orders")
@Validated
public class CustomerOrderController {
    private static final String ROWS = "rows";
    private static final String PAGE_NUMBER = "pageNumber";
    /**
     * CustomerOrderService customerOrder Service.
     */
    private final CustomerOrderService customerOrderService;
    /**
     * HateoasAdder<CustomerOrderDto> hateoasAdder.
     */
    private final HateoasAdder<CustomerOrderDto> hateoasAdder;

    /**
     * The constructor creates a CustomerOrderController object
     *
     * @param customerOrderService CustomerOrderService customerOrder Service
     * @param hateoasAdder         HateoasAdder<CustomerOrderDto> hateoasAdder
     */
    @Autowired
    public CustomerOrderController(CustomerOrderService customerOrderService, @Qualifier("customerOrderHateoasAdder") HateoasAdder<CustomerOrderDto> hateoasAdder) {
        this.customerOrderService = customerOrderService;
        this.hateoasAdder = hateoasAdder;
    }

    /**
     * Method for getting CustomerOrderDto by ID.
     *
     * @param id CustomerOrderDto id
     * @return CustomerOrderDto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerOrderDto getCustomerOrderById(@PathVariable @Positive(message = "ex.customerOrderIdPositive")
                                                 @Digits(integer = 9, fraction = 0, message = "ex.customerOrderIdPositive") String id) {
        CustomerOrderDto customerOrderDto = customerOrderService.findEntityById(Long.parseLong(id));
        hateoasAdder.addLinks(customerOrderDto);
        return customerOrderDto;
    }

    /**
     * Method for getting list of all CustomerOrderDto objects.
     *
     * @param rows       number of lines per page (5 by default)
     * @param pageNumber page number(default 0)
     * @return ResourceDto<CustomerOrderDto>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto<CustomerOrderDto> getCustomerOrderList(@RequestParam(name = PAGE_NUMBER, defaultValue = "1") @Positive(message = "ex.page")
                                                              @Digits(integer = 6, fraction = 0, message = "ex.page") String pageNumber,
                                                              @RequestParam(name = ROWS, defaultValue = "5") @Positive(message = "ex.rows")
                                                              @Digits(integer = 6, fraction = 0, message = "ex.rows") String rows) {
        ResourceDto<CustomerOrderDto> orders = customerOrderService.findListEntities(Integer.parseInt(pageNumber), Integer.parseInt(rows));
        hateoasAdder.addLinksToEntitiesList(orders, Integer.parseInt(rows), Integer.parseInt(pageNumber));
        return orders;
    }


    /**
     * Method for saving new CustomerOrderDto.
     * Annotated by {@link Validated} with parameters CustomerOrderDto.OnCreate.class provides validation of the fields of the CustomerOrderDto object when creating.
     *
     * @param customerOrder CustomerOrderDto customerOrder
     * @return created TagDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrderDto createCustomerOrder(@Validated(CustomerOrderDto.OnCreate.class) @RequestBody CustomerOrderDto customerOrder) {
        CustomerOrderDto customerOrderDto = customerOrderService.createCustomerOrder(customerOrder);
        hateoasAdder.addLinks(customerOrderDto);
        return customerOrderDto;
    }
}
