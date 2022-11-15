package org.ttn.ecommerce.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="user_id")
@Table(name="seller")
public class Seller extends  UserEntity{

    @NotNull
    private String gst;

    @NotNull
    private String companyContact;

    @NotNull
    private String companyName;





}
