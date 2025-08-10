package pg.project.jtasks.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import pg.project.jtasks.Entity.Collection;

import java.util.List;

public interface CollectionRepository extends MongoRepository<Collection, ObjectId> {

    List<Collection> getAllByUserId(ObjectId userId);
}
