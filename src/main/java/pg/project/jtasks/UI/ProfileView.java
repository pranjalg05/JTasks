package pg.project.jtasks.UI;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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

import java.util.concurrent.ThreadLocalRandom;

@Route("/profile")
@PageTitle("Profile")
@PermitAll
public class ProfileView extends VerticalLayout {

    @Autowired
    UserService userService;

    User currentUser;

    Avatar avatar;
    H2 nameLabel;
    TextField usernameField;
    PasswordField passwordField;
    PasswordField confirmPasswordField;
    Button saveButton;
    Button deleteAccountButton;
    ConfirmDialog confirmDialog;
    VerticalLayout content;

    private void setUI() {

        currentUser = CurrentUser.get();

        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        avatar = new Avatar(currentUser.getUsername());
        avatar.setWidth("100px");
        avatar.setHeight("100px");
        avatar.setColorIndex(currentUser.getAvatarColorIndex());

        nameLabel = new H2(currentUser.getUsername());

        usernameField = new TextField("Change Username");
        usernameField.setWidth("300px");

        passwordField = new PasswordField("Change Password");
        passwordField.setWidth("300px");

        confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setWidth("300px");

        saveButton = new Button("Save Changes");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setWidth("300px");

        deleteAccountButton = new Button("Delete Account");
        deleteAccountButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteAccountButton.setWidth("300px");

        content = new VerticalLayout(
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

        confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Delete Account?");
        confirmDialog.setText("Are you sure you want to delete the account? (You will be logged out)");
        confirmDialog.setCancelable(true);
        confirmDialog.setConfirmText("Delete");
        confirmDialog.setConfirmButtonTheme("error primary");

    }

    public ProfileView(@Autowired AuthenticationContext authContext) {

        setUI();
        UI.getCurrent().getElement().setAttribute("theme", "dark");

        saveButton.addClickListener(click -> {
            String username = usernameField.getValue().trim();
            String password = passwordField.getValue().trim();
            String confirmPassword = confirmPasswordField.getValue().trim();

            if (!password.equals(confirmPassword)) {
                Notification.show("Passwords do not match!");
                return;
            }

            if (username.isEmpty() || password.isEmpty()) {
                Notification.show("Please fill in all fields");
                return;
            }

            if (userService.updateUser(currentUser.getId(), username, password)) {
                Notification.show("Details Updated Succesfully!! ");
                User newUser = userService.findByUsername(username);
                CurrentUser.set(newUser);
                setUI();
                UI.getCurrent().getPage().reload();
            } else {
                Notification.show("Error: User already exists");
            }
        });

        deleteAccountButton.addClickListener(click -> {
            confirmDialog.open();
        });

        confirmDialog.addConfirmListener(click -> {
            userService.deleteUser(currentUser.getId());
            CurrentUser.clear();
            authContext.logout();
        });


        add(content);

    }

}
