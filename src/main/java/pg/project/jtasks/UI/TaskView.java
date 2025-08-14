package pg.project.jtasks.UI;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import pg.project.jtasks.Entity.Collection;
import pg.project.jtasks.Entity.Task;
import pg.project.jtasks.Service.CollectionService;

@Route("tasks")
@PermitAll
public class TaskView extends VerticalLayout implements HasUrlParameter<String> {

    @Autowired
    CollectionService collectionService;

    private Collection collection;

    @Override
    public void setParameter(BeforeEvent beforeEvent, String id) {
        ObjectId collectionId = new ObjectId(id);
        collection = collectionService.getCollectionById(collectionId);
        setupUI();
    }

    void setupUI(){
        Grid<Task> grid = new Grid<>(Task.class, true);
        grid.setItems(collectionService.getAllTasksOfCollection(collection.getCollectionId()));
        add(grid);
    }

}
