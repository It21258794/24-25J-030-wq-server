package com.waterboard.waterqualityprediction.dto.user;

import com.waterboard.waterqualityprediction.models.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class UserSpecification {

    public static Specification<User> hasQuery(String _query) {
        return (root, query, cb) -> _query != null ? cb.like(root.get("_query"), "%" +_query+ "%") : null;
    }

    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, cb) -> firstName != null ? cb.equal(root.get("first_name"), firstName) : null;
    }

    public static Specification<User> hasLastName(String lastName) {
        return (root, query, cb) -> lastName != null ? cb.equal(root.get("last_name"), lastName) : null;
    }

    public static Specification<User> hasEmail(String email) {
        return (root, query, cb) -> email != null ? cb.equal(root.get("email"), email) : null;
    }

    public static Specification<User> hasPhone(String phone) {
        return (root, query, cb) -> phone != null ? cb.equal(root.get("phone_with_country_code"), phone) : null;
    }

    public static Specification<User> hasRole(String role) {
        return (root, query, cb) -> role != null ? cb.equal(root.get("role"), role) : null;
    }

    public static Specification<User> hasStatus(String status) {
        return (root, query, cb) -> status != null ? cb.equal(root.get("status"), status) : null;
    }

    public static Specification<User> hasUpdatedAtBetween(String startDate, String endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate != null && endDate != null) {
                try {
                    String decodedStartDate = URLDecoder.decode(startDate, StandardCharsets.UTF_8);
                    String decodedEndDate = URLDecoder.decode(endDate, StandardCharsets.UTF_8);

                    Instant startInstant = Instant.parse(decodedStartDate);
                    Instant endInstant = Instant.parse(decodedEndDate);

                    return criteriaBuilder.between(root.get("updatedAt"), startInstant, endInstant);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid date format", e);
                }
            }
            return null;
        };
    }

    public static Specification<User> isNotSessionUser(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.notEqual(root.get("email"), email);
        };
    }
}
