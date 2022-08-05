package com.solvd.ikaravai.organizationservice.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String AUTH_TOKEN     = "tmx-auth-token";
    public static final String USER_ID        = "tmx-user-id";
    public static final String ORGANIZATION_ID = "tmx-organization-id";

    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();
    private static final ThreadLocal<String> authToken = new ThreadLocal<>();
    private static final ThreadLocal<String> userId= new ThreadLocal<>();
    private static final ThreadLocal<String> organizationId= new ThreadLocal<>();

    public static String getCorrelationId() { return correlationId.get();}

    public static void setCorrelationId(String corrId) {
        correlationId.set(corrId);
    }

    public String getAuthToken() {
        return authToken.get();
    }

    public void setAuthToken(String aToken) {
        authToken.set(aToken);
    }

    public String getUserId() {
        return userId.get();
    }

    public void setUserId(String uId) {
        userId.set(uId);
    }
    public String getOrganizationId() {
        return organizationId.get();
    }
    public void setOrganizationId(String orgId) {
        organizationId.set(orgId);
    }

}
