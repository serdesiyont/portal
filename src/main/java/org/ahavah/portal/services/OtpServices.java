package org.ahavah.portal.services;

import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.otp.SendOTPRequest;
import org.ahavah.portal.mappers.OtpMapper;
import org.ahavah.portal.repositories.OtpRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
@RequiredArgsConstructor
@Service
public class OtpServices {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final OtpRepository otpRepository;
    private final OtpMapper otpMapper;

    @Value("${otp.from}")
    private String SMTP_FROM;

    public Map<String, Object> sendRegisterOtp(SendOTPRequest sendOTPRequest) {
        String subject;
        String templateName;
        if ((sendOTPRequest.getPurpose()).equalsIgnoreCase("register")) {
            subject = "Registeration OTP";
            templateName = "registeration_otp";
        } else if ((sendOTPRequest.getPurpose()).equalsIgnoreCase("forget")) {
            subject = "Password Restore OTP";
            templateName = "password_restore_otp";
        } else {
            subject = "One Time Code";
            templateName = "one_time_code_otp";
        }

        var res = this.sendEmail(sendOTPRequest, templateName, subject);
        if ("SENT".equalsIgnoreCase(String.valueOf(res.get("status")))) {
            var otpEntity = this.otpMapper.toEntity(sendOTPRequest);
            var code = (Integer) res.get("otp"); // key unified as 'otp'
            var expiresAt = (OffsetDateTime) res.get("expiresAt"); // already OffsetDateTime
            otpEntity.setCode(code);
            otpEntity.setExpiresAt(expiresAt);
            this.otpRepository.save(otpEntity);
        }
        return res;
    }

    // Generates an OTP, embeds it in the thymeleaf template, and sends an HTML email.
    // Returns a map with status, otp, expiresAt (OffsetDateTime)
    private Map<String, Object> sendEmail(SendOTPRequest sendOTPRequest, String templateName, String subject) {
        Map<String, Object> resp = new HashMap<>();
        try {
            int otp = ThreadLocalRandom.current().nextInt(100_000, 1_000_000); // 6-digit
            int expiryMinutes = 5;
            OffsetDateTime expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(expiryMinutes);

            Context ctx = new Context();
            ctx.setVariable("name", sendOTPRequest.getName());
            ctx.setVariable("otp", String.valueOf(otp));
            ctx.setVariable("expiry", expiryMinutes);

            String html = templateEngine.process(templateName, ctx);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            helper.setFrom(SMTP_FROM);
            helper.setTo(sendOTPRequest.getEmail());
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

            resp.put("status", "SENT");
            resp.put("otp", otp);
            resp.put("expiresAt", expiresAt); // direct OffsetDateTime
            return resp;
        } catch (Exception e) {
            resp.put("status", "FAILED");
            resp.put("error", e.getMessage());
            return resp;
        }
    }
}
