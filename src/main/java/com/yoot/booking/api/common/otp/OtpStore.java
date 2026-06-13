package com.yoot.booking.api.common.otp;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStore {

    private final Map<String, OtpData> store = new ConcurrentHashMap<>();

    public void save(String email, String otp) {
        store.put(email, new OtpData(otp, System.currentTimeMillis() + 5 * 60 * 1000));
    }

    public String get(String email) {
        OtpData data = store.get(email);
        if (data == null || data.expired()) return null;
        return data.otp;
    }

    public void remove(String email) {
        store.remove(email);
    }

    record OtpData(String otp, long expiredAt) {
        boolean expired() {
            return System.currentTimeMillis() > expiredAt;
        }
    }
}