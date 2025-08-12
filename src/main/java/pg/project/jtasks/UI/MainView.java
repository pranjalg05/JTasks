package pg.project.jtasks.UI;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.shared.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pg.project.jtasks.Entity.User;
import pg.project.jtasks.Repository.UserRepository;
import pg.project.jtasks.Util.CurrentUser;

import java.awt.*;

@Route("")
@PermitAll
public class MainView extends AppLayout{

    final
    UserRepository userRepository;



    public MainView(UserRepository userRepository, @Autowired AuthenticationContext authenticationContext) {
        this.userRepository = userRepository;
        beforeEnter();
        setUpUI(authenticationContext);
    }

    private void setUpUI(AuthenticationContext authenticationContext){
        var currentUser = CurrentUser.get();

        Avatar userAvatar = new Avatar(currentUser.getUsername());
        userAvatar.addThemeVariants(AvatarVariant.LUMO_SMALL);
        userAvatar.setColorIndex((int) Math.random()*7);

        H1 title = new H1("JTasks");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)");

        MenuBar menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);

        MenuItem item = menuBar.addItem(userAvatar);
        SubMenu subMenu = item.getSubMenu();
        subMenu.addItem("Profile", click -> UI.getCurrent().navigate("profile"));
        subMenu.addItem("Sign out", click -> authenticationContext.logout());


        HorizontalLayout topBar = new HorizontalLayout(title, menuBar);
        topBar.setPadding(true);
        topBar.setWidthFull();
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.getStyle()
                .set("padding-left", "1rem")
                .set("padding-right", "1rem")
                .set("padding-top", "0.5rem")
                .set("padding-bottom", "0.5rem");;


        addToNavbar(topBar);
    }


    private void beforeEnter() {
        if (VaadinSession.getCurrent().getAttribute("userLoaded") == null) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            String username = securityContext.getAuthentication().getName();
            if (username != null && !"anonymousUser".equals(username)) {
                var loggedInUser = userRepository.findUserByUsername(username);
                CurrentUser.set(loggedInUser);
                VaadinSession.getCurrent().setAttribute("userLoaded", true); // Mark as done
            }
        }
    }

}
