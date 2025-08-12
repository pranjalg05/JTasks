package pg.project.jtasks.Util;

import com.vaadin.flow.server.VaadinSession;
import pg.project.jtasks.Entity.User;

public class CurrentUser {

    private static final String USER_SESSION_KEY = "loggedInUser";

    public static void set(User user) {
        VaadinSession.getCurrent().setAttribute(USER_SESSION_KEY, user);
    }

    public static User get() {
        return (User) VaadinSession.getCurrent().getAttribute(USER_SESSION_KEY);
    }

    public static void clear() {
        VaadinSession.getCurrent().setAttribute(USER_SESSION_KEY, null);
    }

}
