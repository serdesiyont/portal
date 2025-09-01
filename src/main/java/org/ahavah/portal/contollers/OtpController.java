package org.ahavah.portal.contollers;

import lombok.RequiredArgsConstructor;
import org.ahavah.portal.dtos.otp.SendOTPRequest;
import org.ahavah.portal.services.OtpServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpServices otpServices;

    @PostMapping
    public ResponseEntity<?> sendOtp(
            @RequestBody SendOTPRequest sendOTPRequest){

        return ResponseEntity.ok(this.otpServices.sendRegisterOtp(sendOTPRequest));

    }

}
