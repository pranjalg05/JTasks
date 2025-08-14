package pg.project.jtasks.UI;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.Lumo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pg.project.jtasks.Entity.Collection;
import pg.project.jtasks.Entity.User;
import pg.project.jtasks.Service.CollectionService;
import pg.project.jtasks.Service.UserService;
import pg.project.jtasks.Util.CurrentUser;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route("")
@PermitAll
@UIScope
public class MainView extends AppLayout {

    @Autowired
    UserService userService;

    @Autowired
    CollectionService collectionService;

    @Autowired
    AuthenticationContext authenticationContext;

    private User currentUser;
    private FlexLayout grid;
    private final List<Collection> selectedCollections = new ArrayList<>();
    private ConfirmDialog confirmDialogMulti;

    @PostConstruct
    public void init() {
        beforeEnter();
        setDrawerOpened(false);
        setUpUI();
        UI.getCurrent().getElement().setAttribute("theme", Lumo.DARK);
    }

    private void setUpUI() {

        currentUser = CurrentUser.get();
        createElements();
        setDrawer();
        setTopBar();
        setMain();

    }

    private void createElements(){

        confirmDialogMulti = new ConfirmDialog();
        confirmDialogMulti.setHeader("Delete Collections?");
        confirmDialogMulti.setText("Are you sure you want to delete the collections? (This action cannot be undone)");
        confirmDialogMulti.setCancelable(true);
        confirmDialogMulti.setConfirmText("Delete");
        confirmDialogMulti.setConfirmButtonTheme("error primary");



    }

    private void setTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setPadding(false);
        topBar.setSpacing(true);
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.getStyle().set("padding", "0 var(--lumo-space-m)");

        Avatar userAvatar = new Avatar(currentUser.getUsername());
        userAvatar.addThemeVariants(AvatarVariant.LUMO_LARGE);
        userAvatar.setColorIndex(currentUser.getAvatarColorIndex());
        userAvatar.getStyle().set("margin", "6px");
        userAvatar.getElement().addEventListener("click", click -> setDrawerOpened(!isDrawerOpened()));

        Span greeting = new Span("Hello, " + currentUser.getUsername());
        greeting.getStyle()
                .set("font-size", "var(--lumo-font-size-m)")
                .set("font-weight", "500")
                .set("margin-right", "20px");

        topBar.add(userAvatar);
        topBar.add(new Span());
        topBar.expand(topBar.getComponentAt(1));
        topBar.add(greeting);

        addToNavbar(topBar);
    }


    public void setDrawer() {

        VerticalLayout drawerLayout = new VerticalLayout();
        drawerLayout.setSizeFull();
        drawerLayout.setPadding(false);
        drawerLayout.setSpacing(false);

        VerticalLayout topSection = new VerticalLayout();
        topSection.setPadding(false);
        topSection.setSpacing(false);

        VerticalLayout bottomSection = new VerticalLayout();
        bottomSection.setPadding(false);
        bottomSection.setSpacing(false);

        Div spacer = new Div();
        spacer.setSizeFull();

        Button profilePage = new Button("  Profile", VaadinIcon.USER.create());
        profilePage.setWidthFull();
        profilePage.setHeight("45px");
        profilePage.getStyle()
                .set("border-radius", "7px")
                .set("padding", "0.5rem 1rem");
        profilePage.addClickListener(click -> UI.getCurrent().navigate("profile"));


        Button logoutButton = new Button("Logout", VaadinIcon.EXIT.create());
        logoutButton.setHeight("45px");
        logoutButton.getStyle()
                .set("border-radius", "7px")
                .set("padding", "0.5rem 1rem");
        logoutButton.setWidthFull();
        logoutButton.addClickListener(click -> authenticationContext.logout());

        bottomSection.add(profilePage);
        bottomSection.add(logoutButton);

        addToDrawer(topSection, spacer, bottomSection);
    }

    private Card createCard(Collection collection) {
        Card card = new Card();

        card.setTitle(collection.getCollectionName());
        card.getElement().setAttribute("id", collection.getCollectionId().toHexString());

        card.addThemeVariants(
                CardVariant.LUMO_OUTLINED,
                CardVariant.LUMO_ELEVATED
        );

        card.setWidth("300px");
        card.setHeight("220px");

        int completedTasks = collectionService.numberOfCompletedTasks(collection);
        int totalTasks = collectionService.getTotalTasks(collection);

        Span status = new Span(completedTasks + "/" + totalTasks);
        status.getElement().getThemeList().add("badge success");
        card.setHeaderSuffix(status);

        Span numberOfTasksLeft = new Span((totalTasks - completedTasks) + " Tasks left!");
        card.add(numberOfTasksLeft);

        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.setPadding(false);
        actions.setSpacing(true);


        Button viewCollection = new Button("View");
        viewCollection.addClickListener(click -> {
            UI.getCurrent().navigate("tasks/" + collection.getCollectionId().toHexString());
        });

        Button deleteCollection = new Button("Delete");
        deleteCollection.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteCollection.addClickListener(click -> {
            collectionService.deleteCollection(collection.getCollectionId());
            setCollections();
        });



        Checkbox select = new Checkbox();
        select.addValueChangeListener(e -> {
            if (e.getValue()) selectedCollections.add(collection);
            else selectedCollections.remove(collection);
        });

        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.add(viewCollection, deleteCollection, select);
        card.addToFooter(actions);

        card.getStyle().set("border-radius", "10px");


        return card;
    }

    private void setMain() {

        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        grid = new FlexLayout();
        setCollections();

        Button addButton = new Button(VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(click -> {

            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setSpacing(true);
            layout.setPadding(true);
            layout.setAlignItems(FlexComponent.Alignment.STRETCH);

            Dialog dialogbox = new Dialog();
            dialogbox.setHeaderTitle("Add a new Collection");

            TextField nameField = new TextField("Collection Name");
            nameField.setWidthFull();

            Button saveButton = new Button("Save", e -> {
                String name = nameField.getValue().trim();
                if (!name.isEmpty()) {
                    collectionService.addCollection(currentUser.getId(), name);
                    setCollections();
                    dialogbox.close();
                }
            });

            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            Button cancelButton = new Button("Cancel", ev -> dialogbox.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_ERROR);


            HorizontalLayout options = new HorizontalLayout(cancelButton, saveButton);

            layout.add(nameField, options);
            dialogbox.add(layout);
            dialogbox.open();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(click -> {
            if(selectedCollections.isEmpty()){
                Notification.show("No Collection selected");
            } else {
                confirmDialogMulti.open();
            }
            setCollections();
        });

        confirmDialogMulti.addConfirmListener(click -> {
            for(Collection c: selectedCollections){
                collectionService.deleteCollection(c.getCollectionId());
                setCollections();
            }
            selectedCollections.clear();
        });

        actions.getStyle().set("padding-right", "15px");
        actions.getStyle().set("padding-top", "7px");
        actions.add(deleteButton, addButton);


        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(true);
        mainLayout.add(actions, grid);


        setContent(mainLayout);

    }

    private void setCollections() {
        grid.removeAll();
        grid.getStyle().set("gap", "12px");
        grid.getStyle().set("margin", "10px");
        grid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        grid.setJustifyContentMode(FlexLayout.JustifyContentMode.START);

        List<Collection> collections = userService.getAllCollectionOfUser(currentUser.getId());

        for (Collection c : collections) {
            Card card = createCard(c);
            grid.add(card);
        }

    }


    private void beforeEnter() {
        if (VaadinSession.getCurrent().getAttribute("userLoaded") == null) {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            String username = securityContext.getAuthentication().getName();
            if (username != null && !"anonymousUser".equals(username)) {
                var loggedInUser = userService.findByUsername(username);
                CurrentUser.set(loggedInUser);
                VaadinSession.getCurrent().setAttribute("userLoaded", true); // Mark as done
            }
        }
    }

}
