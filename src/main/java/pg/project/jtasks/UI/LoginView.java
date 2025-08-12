package pg.project.jtasks.UI;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginOverlay loginBox  = new LoginOverlay();

    LoginView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);



        loginBox.setTitle("JTasks");
        loginBox.setDescription("Coding pmos 🥀");
        loginBox.setForgotPasswordButtonVisible(false);
        loginBox.setAction("login");

        var footer = new Paragraph("New here? ");
        footer.add(new Anchor("register", "Create an account!"));
        loginBox.getFooter().add(footer);


        H2 title = new H2("Welcome Back!");

        add(title, loginBox);
        loginBox.setOpened(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginBox.setError(true);
        }
    }

}
