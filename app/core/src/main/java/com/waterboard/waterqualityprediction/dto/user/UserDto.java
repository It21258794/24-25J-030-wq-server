package com.waterboard.waterqualityprediction.dto.user;

import com.waterboard.waterqualityprediction.models.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserDto{
    private String id;
    @Size(max = 40, min = 1, message = "First name length should be more than 1 and less than 40")
    @NotBlank()
    @NotNull()
    private String firstName;

    @Size(max = 40, min = 1, message = "Last name length should be more than 1 and less than 40")
    @NotBlank()
    @NotNull()
    private String lastName;

    @Size(max = 100, min = 6, message = "Email length should be more than 6 and less than 100")
    @NotNull()
    @NotBlank()
    @Email(message = "Email not valid",regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    private String email;

    @Size(max = 15, min = 3, message = "phone length should be more than 3 and less than 15")
    @NotNull()
    @NotBlank()
    private String phone;

    @Size(max = 8, min = 2, message = "Country code should be more than 2 and less than 8")
    @NotNull()
    @NotBlank()
    private String phoneCountryCode;

    @Size(max = 100, min = 6, message = "Password length should be between 6 and 100")
    @NotNull()
    @NotBlank()
    private String password;

    private boolean isPhoneVerified;
    private boolean isEmailVerified;

    @NotNull(message = "status cannot be null")
    @NotBlank(message = "status cannot be blank")
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserDto initBasicDetails(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setStatus(user.getStatus());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setPhoneCountryCode(user.getPhoneCountryCode());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setPhoneVerified(user.isPhoneVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public static UserDto init(User user) {
        UserDto dto = UserDto.initBasicDetails(user);
        dto.setPhoneVerified(user.isPhoneVerified());
        dto.setEmailVerified(user.isEmailVerified());
        return dto;
    }
}
