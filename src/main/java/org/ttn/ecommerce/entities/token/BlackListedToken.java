package org.ttn.ecommerce.entities.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ttn.ecommerce.entities.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BlackListedToken {
    @Id
    @SequenceGenerator(name="blacklisted_token_sequence",sequenceName = "blacklisted_token_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "blacklisted_token_sequence")
    private Long id;

    private String token;

    private LocalDateTime accessTokenExpireAt;

    private Long userId;

    public BlackListedToken(Long id, String token, LocalDateTime accessTokenExpireAt, Long userId) {
        this.id = id;
        this.token = token;
        this.accessTokenExpireAt = accessTokenExpireAt;
        this.userId = userId;
    }
}
