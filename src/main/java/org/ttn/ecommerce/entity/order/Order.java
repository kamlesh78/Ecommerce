package org.ttn.ecommerce.entity.order;

import org.ttn.ecommerce.entity.user.Customer;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Order {

    @Id
    @SequenceGenerator(name="order_sequence",sequenceName = "order_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_sequence")
    private Long id;

    private double amountPaid;

    private Date dateCreated;
    private String paymentMethod;
    private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private String customerAddressAddressLine;
    private String customerAddressZipCode;
    private String customerAddressLabel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_user_id")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    List<OrderProduct> orderProducts;

}
