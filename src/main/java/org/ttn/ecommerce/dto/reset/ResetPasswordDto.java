package org.ttn.ecommerce.dto.reset;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.UniqueEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordDto {

    @NotNull
    @UniqueEmail
    private String email;

    @Password
    @NotNull
    private String password;

    @NotNull
    @NotEmpty
    private String token;


}
