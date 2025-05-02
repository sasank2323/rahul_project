package com.app.config;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    // Initialize the Twilio SDK
    @Bean
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    //192.168.14.79
    //http://ipinfo.io/152.58.232.133/json?token=19ac7dd9c63cbe
}

