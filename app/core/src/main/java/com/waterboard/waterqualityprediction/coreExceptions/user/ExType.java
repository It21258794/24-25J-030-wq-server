package com.waterboard.waterqualityprediction.coreExceptions.user;

import com.waterboard.waterqualityprediction.coreExceptions.ExceptionType;

public enum ExType implements ExceptionType {
    EMAIL_ALREADY_EXISTS,
    EMAIL_ALREADY_VERIFIED,
    MOBILE_ALREADY_EXISTS,
    MOBILE_ALREADY_VERIFIED,
    ORG_NOT_FOUND,
    TEAM_ALREADY_EXISTS,
    TEAM_NOT_FOUND,
    GROUP_ALREADY_EXISTS,
    GROUP_NOT_FOUND,
    USER_ALREADY_EXISTS_EXCEPTION,
    USER_NOT_FOUND,
    UNAUTHORIZED,
    INVALID_INPUT,
    INVALID_EMAIL_OTP,
    INVALID_MOBILE_OTP,
    INVITATION_TOKEN_INVALID,
    INVALID_DEVICE_TYPE,
    CATEGORY_ALREADY_EXISTS,
    CATEGORY_NOT_FOUND,
    GROUPS_NOT_FOUND,
    INVALID_TOKEN,
    TOO_MANY_REQUESTS,
    USER_PACKAGE_NOT_FOUND,
    ACTIVE_PACKAGE_NOT_FOUND,
    API_NOT_AVAILABLE,
    PACKAGE_NOT_FOUND,
    INVALID_LINKED_USER_TYPE,
    PRICE_LIST_EMPTY,
    USER_CANNOT_ADD_NEW_PACKAGE,
    PACKAGE_ALREADY_ACTIVE,
    PASSWORD_ALREADY_CHANGED,
    PACKAGE_INACTIVE,
    PRICE_PACKAGE_INACTIVE,
    CANNOT_DELETE_USER,
    USER_PENDING_TO_DELETE,
    TOO_MANY_FAILED_LOGINS,
    PASSWORD_RESET_TIME_PERIOD_OVER,
    TOO_MANY_PASSWORD_RESET_REQUESTS,
    PASSWORD_RESET_LINK_SEND_BLOCK,
    INCORRECT_PASSWORD,
    ROLE_NOT_FOUND,
    INVALID_ORGANIZATION_USER_STATUS,
    DOCUMENT_LIMIT_EXCEED,
    TEMPLATE_LIMIT_EXCEED,
    USER_LIMIT_EXCEED,
    SUBSCRIPTION_NOT_FOUND,
    USER_NOT_STRIPE_REGISTERED,
    TOO_MANY_MOBILE_VERIFICATION_REQUESTS,
    NOT_STRIPE_SUBSCRIPTION,
    AUTO_RENEWAL_DISABLE,
    INVALID_ROLE,

    USER_NOT_IN_PENDING_STATE,
    INVALID_PACKAGE_PAYMENT_TYPE,
    INVALID_TEAM_STATUS,
    INCORRECT_OTP_CODE,
    INVALID_USER_ACCOUNT;

    @Override
    public String getType() {
        return this.toString();
    }
}