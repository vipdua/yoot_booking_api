package com.yoot.booking.api.service.impl;

import com.yoot.booking.api.entity.Appointment;
import com.yoot.booking.api.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    // ================= PAID =================
    @Override
    public void sendAppointmentPaidEmail(Appointment appointment) {
        sendTemplateEmail(
                appointment,
                "email/appointment-paid",
                "[Yoot] Xác nhận thanh toán lịch hẹn #" + appointment.getId()
        );
    }

    // ================= CONFIRMED =================
    @Override
    public void sendAppointmentConfirmedEmail(Appointment appointment) {
        sendTemplateEmail(
                appointment,
                "email/appointment-confirmed",
                "[Yoot] Lịch hẹn #" + appointment.getId() + " đã được xác nhận"
        );
    }

    // ================= CORE =================
    private void sendTemplateEmail(Appointment appointment, String template, String subject) {

        if (appointment == null || appointment.getUser() == null) {
            throw new IllegalArgumentException("Appointment hoặc User không hợp lệ");
        }

        String to = appointment.getUser().getEmail();

        Context context = new Context();
        context.setVariable("appointment", appointment);

        String html = templateEngine.process(template, context);

        sendHtmlEmail(to, subject, html);
    }

    // ================= HELPER =================
    private void sendHtmlEmail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (MailException | jakarta.mail.MessagingException e) {
            throw new RuntimeException("Send email failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendOtpEmail(String to, String otp) {

        Context context = new Context();
        context.setVariable("otp", otp);

        String html = templateEngine.process("email/otp-email", context);

        String subject = "[Yoot] Mã OTP xác thực";

        sendHtmlEmail(to, subject, html);
    }
}