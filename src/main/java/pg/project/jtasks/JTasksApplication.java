package pg.project.jtasks;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JTasksApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(JTasksApplication.class, args);
    }

}
