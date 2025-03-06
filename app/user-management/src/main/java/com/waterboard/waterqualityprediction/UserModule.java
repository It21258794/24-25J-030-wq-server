package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.dto.user.VerificationDataDto;
import com.waterboard.waterqualityprediction.commonExceptions.UnauthorizeException;
import com.waterboard.waterqualityprediction.coreExceptions.user.ExType;
import com.waterboard.waterqualityprediction.coreExceptions.user.InvalidInputException;
import com.waterboard.waterqualityprediction.coreExceptions.user.UserNotFoundException;
import com.waterboard.waterqualityprediction.models.user.User;
import com.waterboard.waterqualityprediction.services.UserNotificationProxyService;
import com.waterboard.waterqualityprediction.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserModule {

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalAppConfig globalAppConfig;

    @Autowired
    private UserNotificationProxyService userNotificationProxyService;

    public ResultSet<User> getUserByEmail(String email) {
        return ResultSet.of(this.userService.getUserByEmail(email));
    }

    public ResultSet<User> createAdminUser(User user) {
        ResultSet<User> userResultSet = new ResultSet<>(this.userService.createAdminUser(user));
        return userResultSet;
    }

    public ResultSet<User> createUser(User user) {
        ResultSet<User> userResultSet = new ResultSet<>(this.userService.createUser(user));
        return userResultSet;
    }

    public ResultSet<User> userLogin(User loginDetails) {
        User user;
        try {
            user = this.userService.login(loginDetails);
            if(loginDetails.getStatus() != null){
                if(loginDetails.getStatus().equals(User.UserStatus.TO_DELETE) ){
                    throw new UnauthorizeException(ExType.USER_PENDING_TO_DELETE,"invalid login");
                }
            }
        } catch (Exception e) {
            throw e;
        }
        ResultSet<User> userResultSet = new ResultSet<>(user);
        String token = this.userService.userToken(user);
        userResultSet.addExtra(UserModuleExtraKeys.USER_TOKEN, token);
        return userResultSet;
    }

    public HttpHeaders getUserHeaders(ResultSet<User> userResultSet) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (userResultSet.getExtra(UserModuleExtraKeys.USER_TOKEN) != null) {
            httpHeaders.add("Authorization", userResultSet.getExtra(UserModuleExtraKeys.USER_TOKEN));
        }

        return httpHeaders;
    }

    public ResultSet<User> sendUserResetPasswordRequestViaEmail(String email) {
        Optional<User> opUser = this.userService.getUserByEmail(email);
        Check.throwIfEmpty(opUser, new UserNotFoundException("user not found for email " + email));
        ResultSet<User> resultSet = new ResultSet<>(opUser.get());
        this.userService.checkPasswordResetLimit(opUser.get());
        this.userService.updateResetLinkSendCount(opUser.get(),false);
        User updatedUser = userService.addMetaData(opUser.get().getId(), UserModuleExtraKeys.OTP_TOKEN, UUID.randomUUID().toString());
        String token = this.userNotificationProxyService.sendPasswordResetEmailCode(updatedUser);
        resultSet.addExtra(UserModuleExtraKeys.WEB_SERVER_REF, token);
        return resultSet;
    }

    public ResultSet<User> resetPasswordTokenFromOTP(VerificationDataDto mobileVerificationData) {
        JWTContent tokenData = JwtUtil.decode(mobileVerificationData.getServerRef(), globalAppConfig.getSecretKey());
        if (mobileVerificationData.getOtp() == null || !HashUtil.match(mobileVerificationData.getOtp(), tokenData.getPayload().get(UserModuleExtraKeys.HASH))) {
            log.error("invalid otp provided for password reset otp = {}", tokenData.getPayload().get(UserModuleExtraKeys.HASH));
            throw new InvalidInputException("invalid otp provided");
        }
        User user = this.userService.getUserByPasswordResetRequestToken(mobileVerificationData.getServerRef(), UserModuleExtraKeys.RESET_PASSWORD_REQUEST, UserModuleExtraKeys.OTP_TOKEN);
        ResultSet<User> resultSet = new ResultSet<>(user);
        JWTContent content = JWTContent.builder()
                .expiredIn(DateUtils.MILLIS_PER_MINUTE * 10)
                .mainSubject(user.getId())
                .payload(Map.of(UserModuleExtraKeys.ACTION, UserModuleExtraKeys.RESET_PASSWORD,
                        UserModuleExtraKeys.RESET_PASSWORD, user.getMetaData().get(UserModuleExtraKeys.RESET_PASSWORD).toString()
                ))
                .build();
        String passwordResetToken = JwtUtil.encode(content, globalAppConfig.getSecretKey());
        resultSet.addExtra(UserModuleExtraKeys.RESET_PASSWORD_TOKEN, passwordResetToken);
        return resultSet;
    }

    public ResultSet<User> resetPassword(User userDetails, String resetPasswordToken) {
        User user = this.userService.resetPassword(userDetails, resetPasswordToken);
        this.userService.updateResetLinkSendCount(user,true);
        return new ResultSet<>(user);
    }

    public ResultSet<User> sendUserResetPasswordRequestViaSMS(String mobile) {
        Optional<User> opUser = this.userService.getUserByPhoneWithCountryCode(mobile);

        Check.throwIfEmpty(opUser, new UserNotFoundException("user not found for mobile " + mobile));
        ResultSet<User> resultSet = new ResultSet<>(opUser.get());
        this.userService.checkPasswordResetLimit(opUser.get());
        this.userService.updateResetLinkSendCount(opUser.get(),false);
        User updatedUser = userService.addMetaData(opUser.get().getId(), UserModuleExtraKeys.OTP_TOKEN, UUID.randomUUID().toString());
        String token = this.userNotificationProxyService.sendPasswordResetSMS(updatedUser);
        resultSet.addExtra(UserModuleExtraKeys.WEB_SERVER_REF, token);
        return resultSet;
    }
}
