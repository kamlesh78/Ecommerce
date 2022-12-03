package org.ttn.ecommerce.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@Table(name="customer")
@PrimaryKeyJoinColumn(name = "user_id")
 public class Customer  extends  UserEntity{

    @Column(name = "contact")
    private String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
