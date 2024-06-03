package com.ums.ums.service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {
    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public void sendMessage(String to, String body) {
        Twilio.init(accountSid, authToken);
        try {
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber(to),
                            new com.twilio.type.PhoneNumber("+13217326394"),
                            body)
                    .create();

            System.out.println("Message sent: " + message.getSid());
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error sending message: " + e.getMessage());
        }
    }


}
