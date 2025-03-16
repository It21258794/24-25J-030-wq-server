package com.waterboard.waterqualityprediction.repository;

import com.waterboard.waterqualityprediction.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findFirstByEmail(String email);

    Optional<User> findFirstByPhone(String phone);

    Optional<User> findFirstByEmailAndStatusIn(String email, List<String> stringList);

    Optional<User> findFirstByPhoneWithCountryCodeAndStatusIn(String phone, List<String> stringList);

    Optional<User> findFirstByPhoneWithCountryCode(String phone);
}
