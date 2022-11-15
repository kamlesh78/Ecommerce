package org.ttn.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="customer")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer  extends  UserEntity{

    @Column(name = "contact")
    @NotNull
    private String contact;


}
