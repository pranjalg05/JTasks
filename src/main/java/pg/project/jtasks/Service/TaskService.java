package pg.project.jtasks.Service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.project.jtasks.Entity.Task;
import pg.project.jtasks.Repository.CollectionRepository;
import pg.project.jtasks.Repository.TaskRepository;

import java.time.LocalDate;

@Service
@Slf4j
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    CollectionRepository collectionRepository;

    public void addTask(ObjectId collectionId, String title){
        Task task = new Task();
        task.setTitle(title);
        task.setComplete(false);
        task.setDateCreated(LocalDate.now());
        task.setCollectionId(collectionId);
        taskRepository.save(task);
    }



    public void editTask(ObjectId taskId, Task newTask) {
        Task oldtask = taskRepository.findById(taskId).orElse(null);
        if (oldtask == null) {
            log.error("User Doesn't exist");
        }
        try {
            oldtask.setTitle(newTask.getTitle());
            taskRepository.save(oldtask);
        } catch (Exception e) {
            log.error("Error updating collection", e);
        }
    }

    public void updateTask(Task task){
        taskRepository.save(task);
    }

    public String CollectionName(Task task){
        return collectionRepository.findById(task.getCollectionId()).orElse(null).getCollectionName();
    }

    public void deleteTask(ObjectId taskId){
        taskRepository.deleteById(taskId);
    }

}
