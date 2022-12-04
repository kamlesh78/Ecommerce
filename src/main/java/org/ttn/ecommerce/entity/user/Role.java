package org.ttn.ecommerce.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ttn.ecommerce.auditing.Auditable;

import javax.persistence.*;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
public class Role extends Auditable<String> {

    @Id
    @SequenceGenerator(name="role_sequence",sequenceName = "role_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "role_sequence")
    private Long id;

    private String authority;



}

