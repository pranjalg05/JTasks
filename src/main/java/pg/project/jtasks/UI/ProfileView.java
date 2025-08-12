package pg.project.jtasks.UI;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import pg.project.jtasks.Entity.User;
import pg.project.jtasks.Service.UserService;
import pg.project.jtasks.Util.CurrentUser;

@Route("/profile")
@PageTitle("Profile")
@PermitAll
public class ProfileView extends VerticalLayout {

    @Autowired
    UserService userService;

    public ProfileView(@Autowired AuthenticationContext context) {

        var currentUser = CurrentUser.get();

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        Avatar avatar = new Avatar(currentUser.getUsername());
        avatar.setWidth("100px");
        avatar.setHeight("100px");

        H2 nameLabel = new H2(currentUser.getUsername());

        TextField usernameField = new TextField("Change Username");
        usernameField.setWidth("300px");

        PasswordField passwordField = new PasswordField("Change Password");
        passwordField.setWidth("300px");

        PasswordField confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setWidth("300px");

        Button saveButton = new Button("Save Changes");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setWidth("300px");

        Button deleteAccountButton = new Button("Delete Account");
        deleteAccountButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteAccountButton.setWidth("300px");

        saveButton.addClickListener(click -> {
            String username = usernameField.getValue().trim();
            String password = passwordField.getValue().trim();
            String confirmPassword  = confirmPasswordField.getValue().trim();

            if(!password.equals(confirmPassword)){
                Notification.show("Passwords do not match!");
                return;
            }

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Please fill in all fields");
                return;
            }

            if (userService.updateUser(currentUser.getId(), username, password)) {
                Notification.show("Details Updated Succesfully!! Please login again");
                Thread.sleep(1000);
                context.logout();
            } else {
                Notification.show("Error: User already exists");
            }
        });

    VerticalLayout content = new VerticalLayout(
            avatar,
            nameLabel,
            usernameField,
            passwordField,
            confirmPasswordField,
            saveButton,
            deleteAccountButton
    );
        content.setSpacing(true);
        content.setPadding(true);
        content.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    add(content);

}

}
