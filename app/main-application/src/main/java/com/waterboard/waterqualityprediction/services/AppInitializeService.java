package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.AppResources;
import com.waterboard.waterqualityprediction.Result;
import com.waterboard.waterqualityprediction.UserModule;
import com.waterboard.waterqualityprediction.models.user.User;
import io.restassured.path.json.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppInitializeService {

    @Autowired
    private UserModule userModule;

    public void initialize(){
        this.createAdminUserIfNotExists();
    }

    public void createAdminUserIfNotExists() {
        log.info("create admin user if not exists");
        JsonPath adminInfo = JsonPath.from(AppResources.asString("default/admin-info.json").get());
        Result<User> exUserResult = this.userModule.getUserByEmail(adminInfo.getString("email"));
        if (exUserResult.isPresent()) {
            log.info("default admin already exists. skip admin creation");
            return;
        }

        User user = new User();
        user.setFirstName(adminInfo.getString("firstName"));
        user.setLastName(adminInfo.getString("lastName"));
        user.setEmail(adminInfo.getString("email"));
        user.setPassword(adminInfo.getString("password"));
        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setRole(User.UserRoles.SUPER_ADMIN.getRoleName());
        Result<User> userResult = this.userModule.createUser(user);
        log.info("default admin created");
    }
}
