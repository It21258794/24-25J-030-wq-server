package com.waterboard.waterqualityprediction;

import com.waterboard.waterqualityprediction.commonExceptions.http.AccessDeniedException;
import com.waterboard.waterqualityprediction.models.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;

@Slf4j
public class Session {
    public static User getUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof SessionData) {
            return ((SessionData) principal).getUser();
        }
        return null;
    }

    public static void setSessionData(SessionData sessionData) {
        var authorityList = new ArrayList<GrantedAuthority>();
        authorityList.add(new SimpleGrantedAuthority(sessionData.getUser().getRole()));

        Authentication auth = new UsernamePasswordAuthenticationToken(sessionData, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void throwIfNotSuperAdmin() {
        if(getUser() != null && !getUser().isSuperAdmin()) {
            log.error("user does not have super admin access, user = {}.", Session.getUser().getEmail());
            throw new AccessDeniedException("invalid access level");
        }
    }

}
