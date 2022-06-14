package com.example.backend.service;

import com.example.backend.email_feedback.Mail;
import com.example.backend.model.CertificationEntity;
import com.example.backend.model.ForgotPasswordToken;
import com.example.backend.repository.CertificationEntityRepository;
import com.example.backend.repository.ForgotPasswordTokenRepository;
import com.example.backend.service.interfaces.ForgotPasswordTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ForgotPasswordTokenServiceImpl implements ForgotPasswordTokenService {

    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final CertificationEntityRepository certificationEntityRepository;
    private final JavaMailSender mailSender;
    private final Environment env;

    @Override
    public Integer generateToken(String email) throws MessagingException, UnsupportedEncodingException {
        CertificationEntity entity= certificationEntityRepository.findByEmail(email);
        if(entity == null) throw new EntityNotFoundException();
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        LocalDateTime time =LocalDateTime.now().plusHours(2);
        ForgotPasswordToken token = ForgotPasswordToken.builder().token(uuidAsString).email(email).expiringDate(time).build();
        ForgotPasswordToken tokenWithId=forgotPasswordTokenRepository.save(token);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        Mail mail=new Mail();
        mail.setMailFrom("xml.dislinkt@gmail.com");
        mail.setMailTo(email);
        String path = env.getProperty("application.url");
        mail.setMailSubject("Forgot password");
        mail.setMailContent("To change password, please click here : "
                +path+"/auth/forgot-password/"+token.getToken());
        SendMail(mimeMessage, mail);
        return tokenWithId.getId();
    }

    @Override
    public String checkToken(String token) throws Exception {
        ForgotPasswordToken passwordtoken=forgotPasswordTokenRepository.findByToken(token);
        if(passwordtoken ==null) throw new EntityNotFoundException();
        if(passwordtoken.getExpiringDate().isBefore(LocalDateTime.now())) throw new Exception();
        return passwordtoken.getEmail();
    }


    private void SendMail(MimeMessage mimeMessage, Mail mail) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(new InternetAddress(mail.getMailFrom(), "Fishing booker"));
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setText(mail.getMailContent());

            mailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new MessagingException();
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingException();
        }
    }
}
