package canteenManagment.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SmsService {


    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("workflow1@iitm.ac.in"); // Optional, default from config
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
            System.out.println("✅ Email sent to: " + to);
        } catch (Exception e) {
            System.err.println("❌ Error sending email: " + e.getMessage());
        }
    }
   
    private static final String BASE_URL = "https://enterprise.smsgupshup.com/GatewayAPI/rest";
    private static final String USER_ID = "2000230894";
    private static final String PASSWORD = "z6eucW@q"; 
    private static final String SENDER_ID = "SENDERID"; 

    public void sendSms(String phone, String message) {
        try {
            // Encode password and message
            String encodedPassword = URLEncoder.encode(PASSWORD, StandardCharsets.UTF_8);
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

            // Construct the full URL
            String fullUrl = String.format(
                    "%s?v=1.1&method=SendMessage&msg_type=TEXT&userid=%s&auth_scheme=plain&password=%s&send_to=%s&msg=%s&format=text",
                    BASE_URL,
                    USER_ID,
                    encodedPassword,
                    phone,
                    encodedMessage
            );

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(fullUrl, String.class);
            System.out.println("✅ SMS sent successfully. Response: " + response);

        } catch (Exception e) {
            System.err.println("❌ Error sending SMS: " + e.getMessage());
        }
    }
    
}