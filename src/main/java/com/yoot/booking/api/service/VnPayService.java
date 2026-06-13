package com.yoot.booking.api.service;

import com.yoot.booking.api.entity.Appointment;

import java.util.Map;

public interface VnPayService {

    String createPaymentUrl(String txnRef, long amount, String orderInfo, String ip);

    boolean verifyCallback(Map<String, String> params);

}