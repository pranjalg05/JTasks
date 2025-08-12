package pg.project.jtasks.UI;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import pg.project.jtasks.Service.UserService;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    @Autowired
    UserService userService;

    public RegisterView() {
        setAlignItems(Alignment.CENTER);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginOverlay registerBox = new LoginOverlay();
        LoginI18n i18n = LoginI18n.createDefault();

        var registerForm = i18n.getForm();
        var registerHeader = new LoginI18n.Header();

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

        var footer = new Paragraph("Already have an account? ");
        footer.add(new Anchor("login", "Login here!"));
        registerBox.getFooter().add(footer);

        i18n.setHeader(registerHeader);
        i18n.setForm(registerForm);
        registerBox.setI18n(i18n);
        registerBox.setOpened(true);
        add(registerBox);

    }
}
