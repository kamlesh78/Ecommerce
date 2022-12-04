package org.ttn.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MailNotSendException extends RuntimeException {
    public MailNotSendException(String s) {
        super(s);
    }

}
