package com.waterboard.waterqualityprediction.services;

import com.waterboard.waterqualityprediction.ResourceLoader;
import com.waterboard.waterqualityprediction.ResultSet;
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
        JsonPath adminInfo = JsonPath.from(ResourceLoader.asString("default/admin-info.json").get());
        ResultSet<User> exUserResultSet = this.userModule.getUserByEmail(adminInfo.getString("email"));
        if (exUserResultSet.isPresent()) {
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
        ResultSet<User> userResultSet = this.userModule.createUser(user);
        log.info("default admin created");
    }
}
