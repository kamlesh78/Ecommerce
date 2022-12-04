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
public class EmailServiceImpl {


    private JavaMailSender javaMailSender;

    private String toEmail;
    private String subject;
    private String message;


    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(getToEmail());
        mailMessage.setSubject(getSubject());
        mailMessage.setText(getMessage());

        mailMessage.setFrom("kamlesh.singh.ecommerce.project@gmail.com");
        javaMailSender.send(mailMessage);
    }


}
