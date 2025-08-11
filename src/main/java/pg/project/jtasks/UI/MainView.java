package pg.project.jtasks.UI;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Dashboard")
public class MainView extends Div {

    public MainView(){
        add(new H1("main view"));
    }

}
