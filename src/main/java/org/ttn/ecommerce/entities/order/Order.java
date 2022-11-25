package org.ttn.ecommerce.entities.order;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class Order {

    @Id
    @SequenceGenerator(name="order_sequence",sequenceName = "order_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_sequence")
    private Long id;


}
