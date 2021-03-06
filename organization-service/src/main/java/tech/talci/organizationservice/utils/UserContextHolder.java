package tech.talci.organizationservice.utils;

import org.springframework.util.Assert;

public class UserContextHolder {
    private static ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext getContext() {
        UserContext context = userContext.get();

        if (context == null) {
            context = createEmptyContext();
            userContext.set(context);
        }

        return userContext.get();
    }

    public static void setContext(UserContext context) {
        Assert.notNull(context, "Context cannot be null");
        userContext.set(context);
    }

    private static UserContext createEmptyContext() {
        return new UserContext();
    }
}
