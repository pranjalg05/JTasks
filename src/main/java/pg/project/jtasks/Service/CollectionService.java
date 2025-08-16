package pg.project.jtasks.Service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.project.jtasks.Entity.Collection;
import pg.project.jtasks.Entity.CollectionType;
import pg.project.jtasks.Entity.Task;
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

    public void addCollection(ObjectId userId, String name) {
        Collection collection = new Collection();
        collection.setCollectionName(name);
        collection.setUserId(userId);
        collection.setType(CollectionType.CUSTOM);
        collectionRepository.save(collection);
    }



    public void deleteCollection(ObjectId collectionId){
        taskRepository.deleteAllByCollectionId(collectionId);
        collectionRepository.deleteById(collectionId);
    }



    public List<Task> getAllTasksOfCollection(ObjectId collectionId) {
        return taskRepository.getAllByCollectionId(collectionId);
    }

    public int getTotalTasks(Collection collection){
        return getAllTasksOfCollection(collection.getCollectionId()).size();
    }

    public int numberOfCompletedTasks(Collection collection){
        return (int) getAllTasksOfCollection(collection.getCollectionId()).stream().filter(t -> t.isComplete()).count();
    }

    public Collection getCollectionById(ObjectId collectionId){
        return collectionRepository.getCollectionByCollectionId(collectionId);
    }


}
