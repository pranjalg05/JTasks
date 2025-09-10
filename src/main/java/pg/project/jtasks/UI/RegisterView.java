package pg.project.jtasks.UI;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import pg.project.jtasks.Service.UserService;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    @Autowired
    UserService userService;

    private final LoginOverlay registerBox = new LoginOverlay();
    private LoginI18n.Form registerForm;
    private LoginI18n.Header registerHeader;
    private Paragraph footer;

    public RegisterView() {
        UI.getCurrent().getElement().setAttribute("theme", "dark");
        setAlignItems(Alignment.CENTER);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        createElements();

        add(registerBox);

    }

    private void createElements(){
        LoginI18n i18n = LoginI18n.createDefault();

        registerForm = i18n.getForm();
        registerHeader = new LoginI18n.Header();

        registerBox.setForgotPasswordButtonVisible(false);
        registerHeader.setTitle("JTasks");
        registerHeader.setDescription("Tasking made Easy");
        registerForm.setTitle("Create an Account");
        registerForm.setSubmit("Sign-Up");

        registerBox.addLoginListener(e ->
                {
                    String username = e.getUsername().trim();
                    String password = e.getPassword().trim();

                    if (username.isEmpty() || password.isEmpty()) {
                        Notification.show("Please fill in all fields");
                        return;
                    }

                    if (userService.createUser(username, password)) {
                        Notification.show("Account created succesfully");
                        registerBox.setOpened(true);
                        UI.getCurrent().navigate("login");
                    } else {
                        Notification.show("Error: User already exists");
                        registerBox.setOpened(true);
                    }
                }
        );

        footer = new Paragraph("Already have an account? ");
        footer.add(new Anchor("login", "Login here!"));
        registerBox.getFooter().add(footer);

        i18n.setHeader(registerHeader);
        i18n.setForm(registerForm);
        registerBox.setI18n(i18n);
        registerBox.setOpened(true);
    }
}
