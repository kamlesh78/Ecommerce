package org.ttn.ecommerce.services.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
public class EmailServicetry {


    private JavaMailSender javaMailSender;

    @Autowired
    public EmailServicetry(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage simpleMailMessage){



        simpleMailMessage.setFrom("kamlesh.singh.ecommerce.project@gmail.com");
        javaMailSender.send(simpleMailMessage);
    }


}
