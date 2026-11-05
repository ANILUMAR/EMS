package canteenManagment.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import canteenManagment.main.UserEntity.Task;
import canteenManagment.main.userReposerity.TaskRepository;

@Service
public class TaskService {
    @Autowired private TaskRepository taskRepo;

    public List<Task> getAll() {
        return taskRepo.findAll();
    }

    public Task save(Task task) {
        return taskRepo.save(task);
    }
}