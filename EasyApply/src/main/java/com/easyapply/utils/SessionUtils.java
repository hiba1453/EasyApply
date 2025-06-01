package com.easyapply.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class SessionUtils {
    
    public static final String USER_ID_KEY = "userId";
    public static final String USER_ROLE_KEY = "userRole";
    
    public boolean isAuthenticated(HttpSession session) {
        return session != null && session.getAttribute(USER_ID_KEY) != null;
    }
    
    public Long getCurrentUserId(HttpSession session) {
        return (Long) session.getAttribute(USER_ID_KEY);
    }
    
    public String getCurrentUserRole(HttpSession session) {
        return (String) session.getAttribute(USER_ROLE_KEY);
    }
}
