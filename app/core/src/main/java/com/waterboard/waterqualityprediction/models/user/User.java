package com.waterboard.waterqualityprediction.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waterboard.waterqualityprediction.AuditModel;
import com.waterboard.waterqualityprediction.MapToJsonConverter;
import com.waterboard.waterqualityprediction.dto.user.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends AuditModel {

    @Getter
    public enum UserRoles{

        SUPER_ADMIN("SUPER_ADMIN"),
        USER("USER");

        private final String roleName;

        UserRoles(String roleName) {
            this.roleName = roleName;
        }

    }

    public static class UserStatus {
        public static final String ACTIVE = "ACTIVE";
        public static final String BANNED = "BANNED";
        public static final String INACTIVE = "INACTIVE";
        public static final String PENDING_VERIFICATION = "PENDING_VERIFICATION";
        public static final String TO_DELETE = "TO_DELETE";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String phoneCountryCode;
    private String phoneWithCountryCode;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean forcePasswordChange;
    private boolean isPhoneVerified;
    private boolean isEmailVerified;
    private String status;
    private String role;
    @Lob
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> metaData = new HashMap<>();
    private String _query;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Transient
    private String Event;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public static User init(UserDto userDTO) {
        User u = new User();
        u.setFirstName(userDTO.getFirstName());
        u.setLastName(userDTO.getLastName());
        u.setEmail(userDTO.getEmail());
        u.setPhone(userDTO.getPhone());
        u.setPassword(userDTO.getPassword());
        u.setPhoneVerified(userDTO.isPhoneVerified());
        u.setEmailVerified(userDTO.isEmailVerified());
        u.setPhoneCountryCode(userDTO.getPhoneCountryCode());
        u.setPhoneWithCountryCode(userDTO.getPhoneCountryCode() + userDTO.getPhone());
        return u;
    }

    @JsonIgnore
    public void updateQuery(String... values) {
        StringBuilder query = new StringBuilder();
        for (String value : values){
            if (!value.isEmpty()){
                query.append(value).append(" ");
            }
        }
        set_query(query.toString());
    }

    @JsonIgnore
    public boolean isSuperAdmin() {
        if (getRole() == null) {
            return false;
        }
        return getRole().trim().equals(UserRoles.SUPER_ADMIN.getRoleName());
    }

    @JsonIgnore
    public boolean isUser() {
        if (getRole() == null){
            return false;
        }
        return getRole().trim().equals(UserRoles.USER.getRoleName());
    }
}
