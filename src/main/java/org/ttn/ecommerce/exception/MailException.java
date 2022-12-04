package org.ttn.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MailException extends RuntimeException {
    public MailException(String s) {
        super(s);
    }

}
