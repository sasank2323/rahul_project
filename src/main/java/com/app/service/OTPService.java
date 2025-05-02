package com.app.service;


import com.app.entity.OTPDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {
//Collection topic hashMap is used here
    private Map<String, OTPDetails> otpStorage = new HashMap<>();
    private static final int OTP_EXPIRY_TIME = 5;

    public String generateOTP(String mobileNumber){
        String otp = String.format("%06d", new Random().nextInt(999999));
        OTPDetails otpDetails = new OTPDetails(otp, System.currentTimeMillis());
        otpStorage.put(mobileNumber, otpDetails);
        return otp;
    }
}
