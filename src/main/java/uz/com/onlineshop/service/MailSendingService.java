package uz.com.onlineshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.NotAcceptableException;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.entity.user.VerificationEntity;
import uz.com.onlineshop.repository.UserRepository;
import uz.com.onlineshop.repository.VerificationRepository;
import uz.com.onlineshop.response.StandardResponse;
import uz.com.onlineshop.response.Status;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailSendingService {

     private final JavaMailSender javaMailSender;
     private final UserRepository userRepository;
     private final VerificationRepository verificationRepository;
     @Value("${spring.mail.username}")
     private String sender;
    Random random = new Random();





     public StandardResponse<String> sendMessage(String email){
         int message = 100000 + random.nextInt(900000);
         UserEntity userEntity = userRepository.findUserEntityByEmail(email);
         if (userEntity==null){
             throw new NotAcceptableException("Wrong! Did not sign up use this email!");
         }
         SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
         simpleMailMessage.setFrom("sobirjanovobubr768@gmail.com");
         simpleMailMessage.setTo(email);
         simpleMailMessage.setText("Your verification code: " + message);
         System.out.println("verification code: "+message);
         VerificationEntity verificationEntity = verificationRepository.findVerificationEntityByUserId(userEntity.getId());
         if (verificationEntity==null){
             VerificationEntity verification = new VerificationEntity();
             verification.setUserId(userEntity.getId());
             verification.setCode(String.valueOf(message));
             verificationRepository.save(verification);
             return StandardResponse.<String>builder()
                     .data("verification code sent!")
                     .status(Status.SUCCESS)
                     .message("SENT")
                     .build();
         }
         verificationEntity.setCode(String.valueOf(message));
         verificationRepository.save(verificationEntity);

         javaMailSender.send(simpleMailMessage);
         return StandardResponse.<String>builder()
                 .data("verification code sent!")
                 .status(Status.SUCCESS)
                 .message("SENT")
                 .build();
     }
}
