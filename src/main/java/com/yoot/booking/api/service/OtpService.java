package com.yoot.booking.api.service;

public interface OtpService {

    void sendOtp(String email);

    void verifyOtp(String email, String otp);

    void clearOtp(String email);
}