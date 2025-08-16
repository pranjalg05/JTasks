package pg.project.jtasks.UI;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import jakarta.annotation.security.PermitAll;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import pg.project.jtasks.Entity.Collection;
import pg.project.jtasks.Entity.Task;
import pg.project.jtasks.Service.CollectionService;
import pg.project.jtasks.Service.TaskService;

import java.util.List;

@Route("tasks")
@PermitAll
public class TaskView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    CollectionService collectionService;

    @Autowired
    TaskService taskService;

    private Collection collection;
    private VerticalLayout content;
    private Grid<Task> taskGrid;
    private List<Task> taskList;
    private HorizontalLayout topBar;
    private int completedTasks;
    private int totalTasks;
    private H1 title;
    private Span status;
    private Button addButton;


    @Override
    public void setParameter(BeforeEvent beforeEvent, String id) {
        ObjectId collectionId = new ObjectId(id);
        collection = collectionService.getCollectionById(collectionId);
        setupUI();
    }

    private void setupUI(){

        UI.getCurrent().getElement().setAttribute("theme", Lumo.DARK);
        createElements();
        updateNumbers();

        setTasks();
        setTopBar();

        topBar.add(title, status ,addButton);

        content.add(topBar, taskGrid);

        add(content);
    }

    private void setTopBar(){

        title = new H1(collection.getCollectionName());
        title.getStyle().set("font-weight", "700");

        String statusText;
        if (totalTasks == 0) statusText = "Empty";
        else if (totalTasks == completedTasks) statusText = "Completed";
        else statusText = completedTasks + "/" + totalTasks;

        status = new Span(statusText);
        status.getElement().getThemeList().add((totalTasks!=0)?"badge success":"badge error");
        status.setWidth("100px");
        status.setHeight("45px");

        addButton = new Button(VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(click ->{
            VerticalLayout layout = new VerticalLayout();
            layout.setWidth("400px");
            layout.setSpacing(true);
            layout.setPadding(true);
            layout.setAlignItems(FlexComponent.Alignment.STRETCH);

            Dialog dialogbox = new Dialog();
            dialogbox.setHeaderTitle("Add a new Task");

            TextField nameField = new TextField("Task Title");
            nameField.setWidthFull();

            Button saveButton = new Button("Save", e -> {
                String tasktitle = nameField.getValue().trim();
                if(!tasktitle.isEmpty()){
                    taskService.addTask(collection.getCollectionId(), tasktitle);
                    setTasks();
                    updateNumbers();
                    setTopBar();
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

    }

    private void updateNumbers(){
        completedTasks = collectionService.numberOfCompletedTasks(collection);
        totalTasks = collectionService.getTotalTasks(collection);
        setTopBar();
    }

    private void setTasks(){
        taskList = collectionService.getAllTasksOfCollection(collection.getCollectionId());
        taskGrid.setItems(taskList);
    }

    private void createElements(){

        content = new VerticalLayout();
        content.setSizeFull();


        taskGrid = new Grid<>(Task.class, false);
        taskGrid.addColumn(new ComponentRenderer<>(task -> {
            Checkbox checkbox = new Checkbox(task.isComplete());
            checkbox.addValueChangeListener(e -> {
                task.setComplete(e.getValue());
                taskService.updateTask(task);
                updateNumbers();
                setTopBar();
            });
            return checkbox;
        })).setHeader("Status").setWidth("80px").setFlexGrow(0);
        taskGrid.addColumn(Task::getTitle).setHeader("Task");
        taskGrid.addColumn(Task::getDateCreated).setHeader("Date Created");
        taskGrid.addColumn(new ComponentRenderer<>(task -> {
            Button delete = new Button(VaadinIcon.TRASH.create());
            delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            delete.addClickListener(click -> {
                taskService.deleteTask(task.getTaskId());
                setTasks();
            });
            return delete;
        })).setHeader("Delete").setWidth("80px").setFlexGrow(0);

        taskGrid.setEmptyStateText("No Tasks Found");
        taskGrid.addThemeVariants(
                GridVariant.LUMO_COLUMN_BORDERS,
                GridVariant.LUMO_ROW_STRIPES
        );
        taskGrid.setAllRowsVisible(true);
        taskGrid.setSizeFull();

        content.setFlexGrow(1, taskGrid);

        topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        topBar.getStyle().set("padding-right", "15px");
        topBar.setAlignItems(Alignment.CENTER);

    }

}
