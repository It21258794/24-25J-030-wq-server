package com.waterboard.waterqualityprediction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserModuleConfigs {
    @Value("${user.email_verification_required:true}")
    private boolean emailVerificationRequired = true;

    @Value("${user.phone_verification_required:true}")
    private boolean phoneVerificationRequired = true;

    @Value("${user.token_exp_time_web:1}")
    private int tokenExpireTime;

    @Value("${user.token_exp_type_web:hours}")
    private String tokenExpireType = "hours";

    @Value("${user.mobile_email_verification_required:true}")
    private boolean mobileEmailVerificationRequired = true;

    @Value("${user.mobile_phone_verification_required:true}")
    private boolean mobilePhoneVerificationRequired = true;

    @Value("${user.reset_password_link.block_required:true}")
    private boolean blockedResetLinkRequired = true;

    @Value("${user.reset_password_link_allowed_send_attempts:3}")
    private int allowedResetLinkSendAttempts;

    @Value("${user.password_reset_time:2}")
    private int passwordResetTimePeriod;

    @Value("${user.reset_password_link:5}")
    private int resetLinkBlockTime;

}
