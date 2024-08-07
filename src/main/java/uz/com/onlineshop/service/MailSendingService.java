package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailSendingService {

     private final JavaMailSender javaMailSender;
     @Value("${spring.mail.username}")
     private String sender;
    Random random = new Random();
    int message = 100000 + random.nextInt(900000);

     public StandardResponse<String> sendMessage(String email){
         SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
         simpleMailMessage.setFrom("sobirjanovbobur768@gmail.com");
         simpleMailMessage.setTo(email);
         simpleMailMessage.setText("Your verification code: " + message);
         javaMailSender.send(simpleMailMessage);
         return StandardResponse.<String>builder()
                 .data("verification code sent!")
                 .status(Status.SUCCESS)
                 .message("SENT")
                 .build();
     }
}
