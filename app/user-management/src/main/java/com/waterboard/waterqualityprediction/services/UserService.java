package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.*;
import com.waterboard.waterqualityprediction.exceptions.UnauthorizeException;
import com.waterboard.waterqualityprediction.exceptions.user.*;
import com.waterboard.waterqualityprediction.models.user.User;
import com.waterboard.waterqualityprediction.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserModuleConfigs userModuleConfigs;

    @Autowired
    private GlobalConfigs globalConfigs;

    public User save(User user){
        user.set_query("");
        user.updateQuery(
                user.getFirstName() == null ? "" :user.getFirstName()
                ,user.getLastName() == null ? "" : user.getLastName()
                ,user.getEmail() == null ? "" : user.getEmail()
                ,user.getPhone() == null ? "" : user.getPhone()
        );
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        Optional<User> result = this.userRepository.findFirstByEmail(email);
        log.info("get user by email = {}, is available = {}", email, result.isPresent());
        return result;
    }

    public User createUser(User user) {
        log.info("create user = {}", JSON.objectToString(user));
        if (StringUtils.isNotBlank(user.getEmail()) && this.getUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(ExType.EMAIL_ALREADY_EXISTS, "user already exists with email {}" + user.getEmail());
        }
        if (StringUtils.isNotBlank(user.getPhone()) && this.getUserByPhone(user.getPhone()).isPresent()) {
            throw new UserAlreadyExistsException(ExType.MOBILE_ALREADY_EXISTS, "user already exists with phone {} " + user.getPhone());
        }
        if (user.getPassword() != null) user.setPassword(Hash.make(user.getPassword()));
        if (user.getStatus() != null) {
            user.setStatus(user.getStatus());
        } else {
            user.setStatus(User.UserStatus.ACTIVE);
        }
        return userRepository.save(user);
    }
    public Optional<User> getUserByPhone(String phone) {
        Optional<User> result = this.userRepository.findFirstByPhone(phone);
        log.info("get user by phone = {}, is available = {}", phone, result.isPresent());
        return result;
    }

    public Optional<User> getUserById(String id) {
        Optional<User> result = this.userRepository.findById(id);
        log.debug("get user by id = {}, is available = {}", id, result.isPresent());
        return result;
    }

    public User login(User loginDetails) {
        log.info("user login email = {}", loginDetails.getEmail());
        if (StringUtils.isEmpty(loginDetails.getPassword()) || StringUtils.isAllBlank(loginDetails.getEmail(), loginDetails.getPhone())) {
            log.error("login failed. email/phone or password empty. email = {}, phone = {}, password = {}", loginDetails.getEmail(), loginDetails.getPhone(), loginDetails.getPassword());
            throw new UnauthorizeException("login failed. email/phone or password invalid");
        }
        Optional<User> userResult = Optional.empty();
        if (StringUtils.isNotEmpty(loginDetails.getEmail())) {
            log.info("login user with email = {}", loginDetails.getEmail());
            userResult = this.findUserByEmailAndStatus(loginDetails.getEmail(), List.of(User.UserStatus.ACTIVE,User.UserStatus.PENDING_VERIFICATION));
        }
        if (StringUtils.isNotEmpty(loginDetails.getPhone())) {
            log.info("login user with phone = {}", loginDetails.getPhone());
            userResult = this.findUserByPhoneWithCountryCodeAndStatus(loginDetails.getPhone(),List.of(User.UserStatus.ACTIVE, User.UserStatus.PENDING_VERIFICATION));
        }
        if (userResult.isEmpty()) {
            log.error("login failed. user not found for email = {} or phone = {}", loginDetails.getEmail(), loginDetails.getPhone());
            throw new UnauthorizeException("login failed. email/phone or password invalid");
        }
        User user = userResult.get();

        if (!Hash.match(loginDetails.getPassword(), user.getPassword())) {
            String errorMessage = "login failed. email or password invalid";
            if (StringUtils.isNotEmpty(loginDetails.getPhone())) {
                errorMessage = "login failed. phone number or password invalid";
            }
            throw   new UnauthorizeException(errorMessage);
        }

        if ((userModuleConfigs.isEmailVerificationRequired() && !user.isEmailVerified()) ||
                (userModuleConfigs.isPhoneVerificationRequired() && !user.isPhoneVerified()) ||
                (userModuleConfigs.isMobilePhoneVerificationRequired() && !user.isPhoneVerified()) ||
                (userModuleConfigs.isMobileEmailVerificationRequired() && !user.isEmailVerified())) {
            return user;
        }

        log.info("login success email = {}, phone = {}, user = {}", loginDetails.getEmail(), loginDetails.getPhone(), user.getId());
        return user;
    }

    public Optional<User> findUserByEmailAndStatus(String email,List<String> stringList){
        log.info("get user by email = {} and status = {}",email,stringList);
        return this.userRepository.findFirstByEmailAndStatusIn(email, stringList);
    }

    public Optional<User> findUserByPhoneWithCountryCodeAndStatus(String phone,List<String> stringList){
        log.info("get user by phone with country code = {} and status = {}",phone,stringList);
        return this.userRepository.findFirstByPhoneWithCountryCodeAndStatusIn(phone, stringList);
    }

    public String userToken(User user) {
        JWTContent.JWTContentBuilder content = JWTContent.builder().subject(user.getId());
        if (user.getRole() != null){
            content.payload(Map.of("role", user.getRole()));
        }

        float mills;
        switch (userModuleConfigs.getTokenExpireType()) {
            case "minutes":
                mills = DateUtils.MILLIS_PER_MINUTE;
                break;
            case "seconds":
                mills = DateUtils.MILLIS_PER_SECOND;
                break;
            default:
                mills = DateUtils.MILLIS_PER_HOUR;
        }
        content.expiredIn(mills * userModuleConfigs.getTokenExpireTime());

        return JWT.encode(content.build(), globalConfigs.getSecretKey());
    }

    public void  checkPasswordResetLimit(User user){
        if(userModuleConfigs.isBlockedResetLinkRequired() && user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_BLOCK_TIME) != null && user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_ATTEMPTS) != null){

            LocalDateTime blockTime = DateHelper.fromMilliseconds(Long.parseLong(user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_BLOCK_TIME).toString()));
            LocalDateTime passwordResetTimePeriod = DateHelper.fromMilliseconds(Long.parseLong(user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_TIME_PERIOD).toString()));

            if(DateHelper.now().isBefore(passwordResetTimePeriod)){
                if(Integer.parseInt(user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_ATTEMPTS).toString()) >= userModuleConfigs.getAllowedResetLinkSendAttempts()){

                    throw new UnauthorizeException(ExType.TOO_MANY_PASSWORD_RESET_REQUESTS, "too many password reset requests");
                }
            }
            if(Integer.parseInt(user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_ATTEMPTS).toString()) >= userModuleConfigs.getAllowedResetLinkSendAttempts()){
                if(DateHelper.now().isBefore(blockTime)){
                    throw  new UnauthorizeException(ExType.PASSWORD_RESET_LINK_SEND_BLOCK,"too many attempts,password reset block for 30 minutes");
                }
                if(DateHelper.now().isAfter(blockTime)){
                    updateResetLinkSendCount(user,true);
                }

            }

        }
    }

    public User updateResetLinkSendCount(User user, boolean reset) {
        int requestCount;
        if (reset) {
            requestCount = 0;
        } else {
            long passwordResetTimePeriod = DateHelper.toMilliseconds(DateHelper.now().plusMinutes(userModuleConfigs.getPasswordResetTimePeriod()));
            user.getMetaData().put(UserModuleExtraKeys.PASSWORD_RESET_TIME_PERIOD,passwordResetTimePeriod);
            requestCount = user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_ATTEMPTS) != null ? Integer.parseInt(user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_ATTEMPTS).toString()) + 1 : 1;
            if (user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_BLOCK_TIME) != null &&
                    DateHelper.now().isAfter(DateHelper.fromMilliseconds(Long.parseLong(user.getMetaData().get(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_BLOCK_TIME).toString())))) {
                requestCount = 1;
            }
            long blockTime = DateHelper.toMilliseconds(DateHelper.now().plusMinutes(userModuleConfigs.getResetLinkBlockTime()));
            user.getMetaData().put(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_BLOCK_TIME, String.valueOf(blockTime));
        }
        user.getMetaData().put(UserModuleExtraKeys.PASSWORD_RESET_LINK_SEND_ATTEMPTS, String.valueOf(requestCount));
        return this.save(user);
    }

    public User addMetaData(String userId, String key, String value) {
        Optional<User> userOptional = userRepository.findById(userId);
        Check.throwIfEmpty(userOptional, new UserNotFoundException("user not found with ID = " + userId));
        User newUser = userOptional.get();
        newUser.getMetaData().put(key, value);
        return this.save(newUser);
    }

    public User getUserByPasswordResetRequestToken(String resetRequestToken, String action, String metaData) {
        log.info("create password reset token = {}", resetRequestToken);
        JWTContent tokenData = JWT.decode(resetRequestToken, globalConfigs.getSecretKey());
        if (!tokenData.getPayload().getOrDefault(UserModuleExtraKeys.ACTION, "")
                .equalsIgnoreCase(action)) {
            throw new InvalidInputException("Invalid token",ExType.INVALID_TOKEN);
        }
        Optional<User> userResult = this.userRepository.findById(tokenData.getSubject());
        Check.throwIfEmpty(userResult, new UserNotFoundException("user not found for id " + tokenData.getSubject()));
        String passwordResetTokenMetadata = tokenData.getPayload().get(metaData);
        Check.isTrue(passwordResetTokenMetadata.equals(userResult.get().getMetaData().get(metaData)), new InvalidInputException("Invalid token",ExType.INVALID_TOKEN));
        User user = userResult.get();
        user.getMetaData().put(UserModuleExtraKeys.RESET_PASSWORD, UUID.randomUUID().toString());
        return this.save(user);
    }

    public User resetPassword(User userDetails, String resetToken) {
        log.info("reset password with token = {}", resetToken);
        JWTContent tokenData = JWT.decode(resetToken, globalConfigs.getSecretKey());
        if (!tokenData.getPayload().getOrDefault(UserModuleExtraKeys.ACTION, "")
                .equalsIgnoreCase(UserModuleExtraKeys.RESET_PASSWORD)) {
            throw new InvalidInputException("invalid action = " + tokenData.getPayload().getOrDefault(UserModuleExtraKeys.ACTION, ""),ExType.INVALID_TOKEN);
        }
        Optional<User> userResult = this.userRepository.findById(tokenData.getSubject());
        Check.throwIfEmpty(userResult, new UserNotFoundException("user not found " + tokenData.getSubject()));

        String psw_reset_token_uuid = tokenData.getPayload().get(UserModuleExtraKeys.RESET_PASSWORD);
        if (!userResult.get().getMetaData().containsKey(UserModuleExtraKeys.RESET_PASSWORD)) {
            throw new UnauthorizeException(ExType.PASSWORD_ALREADY_CHANGED, "password already changed");
        }
        Check.isTrue(psw_reset_token_uuid.equals(userResult.get().getMetaData().get(UserModuleExtraKeys.RESET_PASSWORD)),
                new InvalidTokenException("Invalid token",ExType.INVALID_TOKEN));

        User user = userResult.get();
        user.getMetaData().remove(UserModuleExtraKeys.RESET_PASSWORD);
        user.getMetaData().remove(UserModuleExtraKeys.EMAIL_TOKEN);
        user.setPassword(Hash.make(userDetails.getPassword()));
        return this.save(user);
    }

    public Optional<User> getUserByPhoneWithCountryCode(String phone) {
        Optional<User> result = this.userRepository.findFirstByPhoneWithCountryCode(phone);
        log.debug("get user by phone = {}, is available = {}", phone, result.isPresent());
        return result;
    }
}
