package pg.project.jtasks.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {

    @Id
    private ObjectId taskId;
    private ObjectId collectionId;
    private String title;
    private boolean isComplete;
    private LocalDateTime dateCreated;

}
