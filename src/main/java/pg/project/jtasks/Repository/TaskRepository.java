package pg.project.jtasks.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import pg.project.jtasks.Entity.Collection;
import pg.project.jtasks.Entity.Task;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, ObjectId> {

    List<Collection> getAllByCollectionId(ObjectId collectionId);

    Task findTaskByTaskId(ObjectId taskId);

    void deleteAllByCollectionId(ObjectId collectionId);
}
