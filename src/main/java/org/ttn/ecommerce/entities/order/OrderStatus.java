package org.ttn.ecommerce.entities.order;

import org.ttn.ecommerce.entities.enums.FromStatus;
import org.ttn.ecommerce.entities.enums.ToStatus;

import javax.persistence.*;


@Entity
@Table(name="order_status")
public class OrderStatus {

    @Id
    @SequenceGenerator(name="order_status_sequence",sequenceName = "order_status_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_status_sequence")
    private Long id;

    @Column(name = "from_status")
    @Enumerated(EnumType.STRING)
    private FromStatus fromStatus;

    @Column(name = "to_status")
    @Enumerated(EnumType.STRING)
    private ToStatus toStatus;

    @Column(name="transition_notes_comments")
    private String transitionNotesComments;

    @OneToOne
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;


}
