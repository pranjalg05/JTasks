package pg.project.jtasks.Service;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pg.project.jtasks.Entity.Task;
import pg.project.jtasks.Repository.TaskRepository;

@Service
@Slf4j
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public void addTask(ObjectId collectionId, Task task){
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

    public void switchTaskStatus(ObjectId taskId){
        Task task = taskRepository.findTaskByTaskId(taskId);
        task.setComplete(!task.isComplete());
        taskRepository.save(task);
    }

    public void deleteTask(ObjectId taskId){
        taskRepository.deleteById(taskId);
    }

}
