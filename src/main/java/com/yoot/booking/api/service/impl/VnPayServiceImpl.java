package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.config.VnPayConfig;
import com.yoot.booking.api.service.VnPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VnPayServiceImpl implements VnPayService {

    private final VnPayConfig config;

    @Override
    public String createPaymentUrl(String txnRef, long amountVnd, String orderInfo, String clientIp) {

        String amount = String.valueOf(amountVnd * 100L);

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", config.getTmnCode());
        params.put("vnp_Amount", amount);
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", config.getReturnUrl());
        params.put("vnp_IpAddr", clientIp);
        params.put("vnp_CreateDate", getCurrentDateTime());
        params.put("vnp_ExpireDate", getExpireDateTime(15));

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<Map.Entry<String, String>> itr = params.entrySet().iterator();
        while (itr.hasNext()) {
            var entry = itr.next();

            hashData.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));

            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));

            if (itr.hasNext()) {
                hashData.append("&");
                query.append("&");
            }
        }

        String secureHash = hmacSha512(config.getHashSecret(), hashData.toString());

        return config.getUrl() + "?" + query + "&vnp_SecureHash=" + secureHash;
    }

    @Override
    public boolean verifyCallback(Map<String, String> params) {

        String receivedHash = params.get("vnp_SecureHash");
        if (receivedHash == null) return false;

        Map<String, String> filtered = new TreeMap<>();
        params.forEach((k, v) -> {
            if (!k.equals("vnp_SecureHash") && !k.equals("vnp_SecureHashType")) {
                filtered.put(k, v);
            }
        });

        StringBuilder hashData = new StringBuilder();
        Iterator<Map.Entry<String, String>> itr = filtered.entrySet().iterator();

        while (itr.hasNext()) {
            var entry = itr.next();

            hashData.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));

            if (itr.hasNext()) hashData.append("&");
        }

        String calculated = hmacSha512(config.getHashSecret(), hashData.toString());

        return calculated.equalsIgnoreCase(receivedHash);
    }

    // ================= PRIVATE =================

    private String hmacSha512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(key.getBytes(), "HmacSHA512"));

            byte[] hash = mac.doFinal(data.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi ký VNPay", e);
        }
    }

    private String getCurrentDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private String getExpireDateTime(int minutes) {
        return LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
                .plusMinutes(minutes)
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}