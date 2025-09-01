package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.otp.SendOTPRequest;
import org.ahavah.portal.entities.Otp;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OtpMapper {

    Otp toEntity(SendOTPRequest sendOTPRequest);

}
