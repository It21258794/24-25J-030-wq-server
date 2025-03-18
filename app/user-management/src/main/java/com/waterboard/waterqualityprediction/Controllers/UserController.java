package com.waterboard.waterqualityprediction.Controllers;

import com.waterboard.waterqualityprediction.*;
import com.waterboard.waterqualityprediction.annotations.LimitPerIP;
import com.waterboard.waterqualityprediction.annotations.OnlySuperAdmin;
import com.waterboard.waterqualityprediction.dto.user.PasswordResetTokenDto;
import com.waterboard.waterqualityprediction.dto.user.UserDto;
import com.waterboard.waterqualityprediction.dto.user.VerificationDataDto;
import com.waterboard.waterqualityprediction.models.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserModule userModule;

    @Autowired
    private GlobalAppConfig globalAppConfig;

    @PostMapping("/login")
    @LimitPerIP(limit = 10, event = UserModuleEvents.USER_LOGIN_REQUEST)
    public ResponseEntity<UserDto> userLogin(@RequestBody UserDto loginDetails) {
        ResultSet<User> userResultSet = this.userModule.userLogin(User.init(loginDetails));
        SessionData sessionData = new SessionData();
        sessionData.setUser(userResultSet.get());
        Session.setSessionData(sessionData);
        UserDto userDto = UserDto.init(userResultSet.get());
        return ResponseEntity.ok()
                .headers(this.userModule.getUserHeaders(userResultSet))
                .body(userDto);
    }

    @PostMapping("/password-reset/otp")
    @LimitPerIP(limit = 5, event = UserModuleEvents.USER_EMAIL_OTP_REQUEST)
    public ResponseEntity sendResetPasswordEmailCode(@RequestBody UserDto emailDetails) {
        ResultSet<User> resultSet = this.userModule.sendUserResetPasswordRequestViaEmail(emailDetails.getEmail());
        return ResponseEntity
                .ok()
                .body(new VerificationDataDto(resultSet.getExtra(UserModuleExtraKeys.WEB_SERVER_REF)));
    }

    @PostMapping("/password-reset/send-otp/sms")
    @LimitPerIP(limit = 5, event = UserModuleEvents.USER_EMAIL_TOKEN_REQUEST)
    public ResponseEntity<VerificationDataDto> sendResetPasswordSMS(@RequestBody UserDto phoneDetails,  @RequestHeader(value = "vi-key",required = false) String ketStoreId) {
        ResultSet<User> resultSet = this.userModule.sendUserResetPasswordRequestViaSMS(phoneDetails.getPhone());
        return ResponseEntity
                .ok()
                .body(new VerificationDataDto(resultSet.getExtra(UserModuleExtraKeys.WEB_SERVER_REF)));
    }

    @PostMapping("/password-reset/token")
    @LimitPerIP(limit = 5, event = UserModuleEvents.USER_OTP_VERIFY_REQUEST)
    public ResponseEntity<PasswordResetTokenDto> createPasswordResetToken(@RequestBody VerificationDataDto verificationDataDto) {
        ResultSet<User> resultSet = this.userModule.resetPasswordTokenFromOTP(verificationDataDto);
        return ResponseEntity.ok(new PasswordResetTokenDto(resultSet.getExtra(UserModuleExtraKeys.RESET_PASSWORD_TOKEN)));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<UserDto> resetPassword(@RequestParam("token") String token, @RequestBody UserDto passwordDetails) {
        ResultSet<User> resultSet = this.userModule.resetPassword(User.init(passwordDetails), token);
        return ResponseEntity.ok(UserDto.init(resultSet.get()));
    }

    @PostMapping("/password-change")
    public ResponseEntity<UserDto> changePassword(@RequestBody UserDto passwordDetails) {
        ResultSet<User> resultSet = this.userModule.changePassword(User.initWithTepPassword(passwordDetails));
        return ResponseEntity.ok(UserDto.init(resultSet.get()));
    }

    @PostMapping("/current/password-change")
    public ResponseEntity<UserDto> changeCurrentPassword(@RequestBody UserDto passwordDetails) {
        ResultSet<User> resultSet = this.userModule.changePassword(User.initWithTepPassword(passwordDetails));
        return ResponseEntity.ok(UserDto.init(resultSet.get()));
    }

    @PostMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(@RequestBody UserDto user) {
        ResultSet<User> resultSet = this.userModule.updateProfile(user);
        return ResponseEntity.ok(UserDto.init(resultSet.get()));
    }

    @PostMapping()
    @OnlySuperAdmin
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        ResultSet<User> resultSet = this.userModule.createUser(user);
        return ResponseEntity.ok(UserDto.init(resultSet.get()));
    }

    @GetMapping()
    public ResponseEntity<UserDto> getUser() {
        ResultSet<User> resultSet = this.userModule.getUser(Session.getUser());
        return ResponseEntity.ok(UserDto.init(resultSet.get()));
    }

    @GetMapping("/users")
    @OnlySuperAdmin
    public Page<UserDto> getUsers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ){
        return userModule.getUsersBySearch(query,firstName,lastName,email,phone,role,status,startDate,endDate,page,size, sortDirection, Session.getUser().getEmail());
    }

        
    @PostMapping("/status-change/{id}/{status}")
    public ResponseEntity<UserDto> changeUserStatusAdmin(@PathVariable("id") String id, @PathVariable("status") String status) {
        ResultSet<User> resultSet = this.userModule.changeUserStatus(id,status);
        return ResponseEntity.ok(UserDto.init(resultSet.get()));
    }
}
