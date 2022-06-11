package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * The class {@code Customer} represents Customer entity.
 *
 * @author Dzmitry Rozmysl
 * @version 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "customer")
@Audited
@AllArgsConstructor
public class Customer extends BaseEntity<Long> {
    /**
     * String name.
     */
    private String name;

    /**
     * String password.
     */
    private String password;

    /**
     * String email.
     */
    private String email;

    /**
     * Role role.
     */
    @Enumerated(value = EnumType.STRING)
    private Role role;

    /**
     * List<CustomerOrder> customerOrder.
     */
    @OneToMany(mappedBy = "customerId")
    private List<CustomerOrder> customerOrders = new ArrayList<>();

    /**
     * The constructor creates a Customer object
     *
     * @param id             long id
     * @param name           String name
     * @param customerOrders List<CustomerOrder> customerOrders
     */
    public Customer(long id, String name, List<CustomerOrder> customerOrders) {
        super(id);
        this.name = name;
        this.customerOrders = customerOrders;
    }

    /**
     * The constructor creates a Customer object
     *
     * @param id long id
     */
    public Customer(long id) {
        super(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        Customer customer = (Customer) o;

        if (getName() != null ? !getName().equals(customer.getName()) : customer.getName() != null) {
            return false;
        }
        if (getPassword() != null ? !getPassword().equals(customer.getPassword()) : customer.getPassword() != null) {
            return false;
        }
        if (getEmail() != null ? !getEmail().equals(customer.getEmail()) : customer.getEmail() != null) {
            return false;
        }
        return getRole() == customer.getRole();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .add("email='" + email + "'")
                .add("role=" + role)
                .add("customerOrders=" + customerOrders)
                .toString();
    }
}
