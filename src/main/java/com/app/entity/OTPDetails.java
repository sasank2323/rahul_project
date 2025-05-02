package com.app.entity;

public class OTPDetails {

    private String otp;
    private long timeStamp;

    public OTPDetails(String otp, long timeStamp){
        this.otp = otp;
        this.timeStamp = timeStamp;
    }

    public String getOtp() {
        return otp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
