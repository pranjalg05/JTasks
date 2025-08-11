package pg.project.jtasks.Entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "collections")
public class Collection {

    @Id
    private ObjectId collectionId;
    private ObjectId userId;
    private String collectionName;
    private CollectionType type;

}
