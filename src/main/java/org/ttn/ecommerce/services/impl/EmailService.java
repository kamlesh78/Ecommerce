package org.ttn.ecommerce.services.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.exception.MailNotSendException;

@Service
@Getter
@Setter
@NoArgsConstructor
public class EmailService {


  //  private JavaMailSender javaMailSender;

    @Autowired
//     public EmailService(JavaMailSender javaMailSender) {
//         this.javaMailSender = javaMailSender;
//     }

    @Async
    public void sendEmail(String toMail, String subject, String message) throws MailNotSendException {
//         SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//         simpleMailMessage.setTo(toMail);
//         simpleMailMessage.setSubject(subject);
//         simpleMailMessage.setText(message);

//         try {

//             javaMailSender.send(simpleMailMessage);
//         }
//         catch(MailException ex)
//         {
//             throw new MailNotSendException("Can not send mail! Mailing Server is down");
//         }
    }




}
