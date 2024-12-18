package com.waterboard.waterqualityprediction.Controllers;

import com.waterboard.waterqualityprediction.*;
import com.waterboard.waterqualityprediction.dto.user.PasswordResetTokenDto;
import com.waterboard.waterqualityprediction.dto.user.UserDto;
import com.waterboard.waterqualityprediction.dto.user.VerificationDataDto;
import com.waterboard.waterqualityprediction.models.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserModule userModule;

    @Autowired
    private GlobalConfigs globalConfigs;


    @PostMapping("/login")
    public ResponseEntity<UserDto> userLogin(@RequestBody UserDto loginDetails) {
        Result<User> userResult = this.userModule.userLogin(User.init(loginDetails));
        SessionData sessionData = new SessionData();
        sessionData.setUser(userResult.get());
        Session.setSessionData(sessionData);
        UserDto userDto = UserDto.init(userResult.get());
        return ResponseEntity.ok()
                .headers(this.userModule.getUserHeaders(userResult))
                .body(userDto);
    }

    @PostMapping("/password-reset/otp")
//    @LimitPerIP(limit = 5, event = UserModuleEvents.USER_EMAIL_OTP_REQUEST)
    public ResponseEntity sendResetPasswordEmailCode(@RequestBody UserDto emailDetails) {
        Result<User> result = this.userModule.sendUserResetPasswordRequestViaEmail(emailDetails.getEmail());
        return ResponseEntity
                .ok()
                .body(new VerificationDataDto(result.getExtra(UserModuleExtraKeys.WEB_SERVER_REF)));
    }

    @PostMapping("/password-reset/send-otp/sms")
//    @LimitPerIP(limit = 5, event = UserModuleEvents.USER_MOBILE_OTP_REQUEST)
    public ResponseEntity<VerificationDataDto> sendResetPasswordSMS(@RequestBody UserDto phoneDetails,  @RequestHeader(value = "vi-key",required = false) String ketStoreId) {
        Result<User> result = this.userModule.sendUserResetPasswordRequestViaSMS(phoneDetails.getPhone());
        return ResponseEntity
                .ok()
                .body(new VerificationDataDto(result.getExtra(UserModuleExtraKeys.WEB_SERVER_REF)));
    }

    @PostMapping("/password-reset/token")
//    @LimitPerIP(limit = 5, event = UserModuleEvents.USER_OTP_VERIFY_REQUEST)
    public ResponseEntity<PasswordResetTokenDto> createPasswordResetToken(@RequestBody VerificationDataDto verificationDataDto) {
        Result<User> result = this.userModule.resetPasswordTokenFromOTP(verificationDataDto);
        return ResponseEntity.ok(new PasswordResetTokenDto(result.getExtra(UserModuleExtraKeys.RESET_PASSWORD_TOKEN)));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<UserDto> resetPassword(@RequestParam("token") String token, @RequestBody UserDto passwordDetails) {
        Result<User> result = this.userModule.resetPassword(User.init(passwordDetails), token);
        return ResponseEntity.ok(UserDto.init(result.get()));
    }
}
