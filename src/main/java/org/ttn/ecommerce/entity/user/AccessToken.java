package org.ttn.ecommerce.entity.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttn.ecommerce.auditing.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "access_token")
public class AccessToken extends Auditable<String> {
    @Id
    @SequenceGenerator(name="token_sequence",sequenceName = "user_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private Long id;

    @Column(name="token")
    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private UserEntity userEntity;

    public AccessToken(Long id, String token, LocalDateTime createdAt, LocalDateTime expiredAt, UserEntity userEntity) {
        this.id = id;
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.userEntity = userEntity;
    }
}
