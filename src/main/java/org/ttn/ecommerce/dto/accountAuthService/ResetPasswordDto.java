package org.ttn.ecommerce.dto.accountAuthService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordDto {

    private String email;

    private String password;

    private String token;


}
