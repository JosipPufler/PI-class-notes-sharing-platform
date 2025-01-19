package hr.algebra.pi.services;

import hr.algebra.pi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    Environment env;
    JavaMailSender mailSender;

    @Autowired
    public MailService(Environment env, JavaMailSender mailSender) {
        this.env = env;
        this.mailSender = mailSender;
    }

    public void SendMail(String destinationMail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinationMail);
        message.setSubject(subject);
        message.setFrom(env.getProperty("spring.mail.username"));
        message.setText(text);
        (new Thread(() -> mailSender.send(message))).start();
    }

    public String Generate2FAMessage(User user, String code) {
        return new StringBuilder("Hello ").append(user.getUsername()).append(" your verification code is: ").append(code).toString();
    }
}
