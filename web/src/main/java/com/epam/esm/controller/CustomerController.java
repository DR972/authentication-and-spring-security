package com.epam.esm.controller;

import com.epam.esm.dto.CustomerOrderDto;
import com.epam.esm.dto.ResourceDto;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.CustomerService;
import com.epam.esm.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Positive;

/**
 * Class {@code CustomerController} is an endpoint of the API which allows you to perform operations on Customers.
 * Annotated by {@link RestController} without parameters to provide an answer in application/json.
 * Annotated by {@link RequestMapping} with parameter value = "/customers".
 * Annotated by {@link Validated} without parameters  provides checking of constraints in method parameters.
 * So that {@code CustomerController} is accessed by sending request to /customers.
 *
 * @author Dzmitry Rozmysl
 * @since 1.0
 */
@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {
    private static final String ROWS = "rows";
    private static final String PAGE_NUMBER = "pageNumber";
    /**
     * CustomerService customerService.
     */
    private final CustomerService customerService;
    /**
     * HateoasAdder<CustomerDto> customerHateoasAdder.
     */
    private final HateoasAdder<CustomerDto> customerHateoasAdder;
    /**
     * HateoasAdder<CustomerOrderDto> orderHateoasAdder.
     */
    private final HateoasAdder<CustomerOrderDto> orderHateoasAdder;

    /**
     * The constructor creates a CustomerController object
     *
     * @param customerService      CustomerService customerService
     * @param customerHateoasAdder HateoasAdder<CustomerDto> customerHateoasAdder
     * @param orderHateoasAdder    HateoasAdder<CustomerOrderDto> orderHateoasAdder
     */
    @Autowired
    public CustomerController(CustomerService customerService, @Qualifier("customerHateoasAdder") HateoasAdder<CustomerDto> customerHateoasAdder,
                              @Qualifier("customerOrderHateoasAdderToCustomer") HateoasAdder<CustomerOrderDto> orderHateoasAdder) {
        this.customerService = customerService;
        this.customerHateoasAdder = customerHateoasAdder;
        this.orderHateoasAdder = orderHateoasAdder;
    }

    /**
     * Method for getting CustomerDto by ID.
     *
     * @param id CustomerDto id
     * @return CustomerDto
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerById(@PathVariable @Positive(message = "ex.customerIdPositive")
                                       @Digits(integer = 9, fraction = 0, message = "ex.customerIdPositive") String id) {
        CustomerDto customerDto = customerService.findEntityById(Long.parseLong(id));
        customerHateoasAdder.addLinks(customerDto);
        return customerDto;
    }

    /**
     * Method for getting list of all CustomerDto objects.
     *
     * @param rows       number of lines per page (5 by default)
     * @param pageNumber page number(default 0)
     * @return ResourceDto<CustomerDto>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto<CustomerDto> getCustomerList(@RequestParam(name = PAGE_NUMBER, defaultValue = "1") @Positive(message = "ex.page")
                                                    @Digits(integer = 6, fraction = 0, message = "ex.page") String pageNumber,
                                                    @RequestParam(name = ROWS, defaultValue = "5") @Positive(message = "ex.rows")
                                                    @Digits(integer = 6, fraction = 0, message = "ex.rows") String rows) {
        ResourceDto<CustomerDto> customers = customerService.findListEntities(Integer.parseInt(pageNumber), Integer.parseInt(rows));
        customerHateoasAdder.addLinksToEntitiesList(customers, Integer.parseInt(rows), Integer.parseInt(pageNumber));
        return customers;
    }

    /**
     * Method for getting CustomerOrderDto by Customer id and CustomerOrder id.
     *
     * @param customerId CustomerDto customerId
     * @param orderId    CustomerOrderDto orderId
     * @return CustomerOrderDto
     */
    @GetMapping("{customerId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerOrderDto getCustomerOrderByCustomerIdAndOrderId(@PathVariable @Positive(message = "ex.customerIdPositive")
                                                                   @Digits(integer = 9, fraction = 0, message = "ex.customerIdPositive") String customerId,
                                                                   @PathVariable @Positive(message = "ex.customerOrderIdPositive")
                                                                   @Digits(integer = 9, fraction = 0, message = "ex.customerOrderIdPositive") String orderId) {
        CustomerOrderDto customerOrderDto = customerService.findCustomerOrderByCustomerIdAndOrderId(customerId, orderId);
        orderHateoasAdder.addLinks(customerOrderDto);
        return customerOrderDto;
    }

    /**
     * Method for getting list of CustomerOrderDto objects.
     *
     * @param customerId CustomerDto customerId
     * @param rows       number of lines per page (5 by default)
     * @param pageNumber page number(default 0)
     * @return ResourceDto<CustomerOrderDto>
     */
    @GetMapping("{customerId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public ResourceDto<CustomerOrderDto> getCustomerOrderList(@PathVariable @Positive(message = "ex.customerIdPositive")
                                                              @Digits(integer = 9, fraction = 0, message = "ex.customerIdPositive") String customerId,
                                                              @RequestParam(name = PAGE_NUMBER, defaultValue = "1") @Positive(message = "ex.page")
                                                              @Digits(integer = 6, fraction = 0, message = "ex.page") String pageNumber,
                                                              @RequestParam(name = ROWS, defaultValue = "5") @Positive(message = "ex.rows")
                                                              @Digits(integer = 6, fraction = 0, message = "ex.rows") String rows) {
        ResourceDto<CustomerOrderDto> orders = customerService.findListCustomerOrdersByCustomerId(customerId, Integer.parseInt(pageNumber), Integer.parseInt(rows));
        orderHateoasAdder.addLinksToEntitiesList(orders, Integer.parseInt(rows), Integer.parseInt(pageNumber), Integer.parseInt(customerId));
        return orders;
    }
}
