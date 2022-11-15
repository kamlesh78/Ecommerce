package org.ttn.ecommerce.entities;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class RefreshToken {
    @Id
    @SequenceGenerator(name="user_sequence",sequenceName = "user_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long id;

    private String token;

    private LocalDateTime expireAt;

    @OneToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private UserEntity userEntity;


}
