package pg.project.jtasks.Service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.project.jtasks.Entity.Collection;
import pg.project.jtasks.Repository.CollectionRepository;
import pg.project.jtasks.Repository.TaskRepository;

import java.util.List;

@Service
@Slf4j
public class CollectionService {

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    TaskRepository taskRepository;

    public void addCollection(Collection collection) {
        collectionRepository.save(collection);
    }

    public void editCollection(ObjectId collectionId, Collection newCollection) {
        Collection collectionInDb = collectionRepository.findById(collectionId).orElse(null);
        if (collectionInDb == null) {
            log.error("User Doesn't exist");
            return;
        }
        try {
            collectionInDb.setCollectionName(newCollection.getCollectionName());
            collectionRepository.save(collectionInDb);
        } catch (Exception e) {
            log.error("Error updating collection", e);
        }
    }

    public void deleteTask(ObjectId collectionId){
        collectionRepository.deleteById(collectionId);
    }

    public List<Collection> getAllTasksOfCollection(ObjectId collectionId) {
        return taskRepository.getAllByCollectionId(collectionId);
    }

}
