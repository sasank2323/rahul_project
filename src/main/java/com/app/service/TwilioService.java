package com.app.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    // Method to send an SMS using Twilio API
    public void sendSms(String toPhoneNumber, String messageBody) {

            // Send the message through Twilio API
            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),    // Recipient's phone number
                    new PhoneNumber(twilioPhoneNumber),// Sender's Twilio phone number
                    messageBody                        // Message content
            ).create();


    }
}

