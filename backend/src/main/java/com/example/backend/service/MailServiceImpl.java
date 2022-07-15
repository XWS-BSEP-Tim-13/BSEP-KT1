package com.example.backend.service;

import com.example.backend.email_feedback.Mail;
import com.example.backend.model.PasswordlessCredentials;
import com.example.backend.service.interfaces.MailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(MimeMessage mimeMessage, Mail mail) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "PKI"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());

            mailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new MessagingException();
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException();
        }
    }

    @Override
    public void sendPasswordlessCode(PasswordlessCredentials credentials) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Mail mail=new Mail();
        mail.setMailFrom("xml.dislinkt@gmail.com");
        mail.setMailTo(credentials.getEmail());
        mail.setMailSubject("Passwordless code");
        mail.setMailContent("The code is: " + credentials.getCode());
        sendMail(mimeMessage, mail);
    }
}
