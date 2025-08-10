package pg.project.jtasks.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import pg.project.jtasks.Entity.User;

public interface UserRepository extends MongoRepository<User, ObjectId> {

}
