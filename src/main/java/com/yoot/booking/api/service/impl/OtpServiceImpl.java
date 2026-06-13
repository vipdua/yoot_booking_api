package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.common.otp.OtpStore;
import com.yoot.booking.api.service.EmailService;
import com.yoot.booking.api.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpStore otpStore;
    private final EmailService emailService;

    @Override
    public void sendOtp(String email) {

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        otpStore.save(email, otp);

        emailService.sendOtpEmail(email, otp);
    }

    @Override
    public void verifyOtp(String email, String otp) {

        String stored = otpStore.get(email);

        if (stored == null || !stored.equals(otp)) {
            throw new RuntimeException("OTP không hợp lệ");
        }
    }

    @Override
    public void clearOtp(String email) {
        otpStore.remove(email);
    }
}