package canteenManagment.main.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import canteenManagment.main.UserEntity.Task;
import canteenManagment.main.security.AuthContext;
import canteenManagment.main.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * Admins read the whole board; every other role only receives the tasks that
     * are assigned to them, so the dashboards of the three logins differ.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(Authentication authentication) {
        List<Task> tasks = taskService.getAll();

        if (!AuthContext.isAdmin(authentication)) {
            String email = AuthContext.email(authentication);
            tasks = tasks.stream()
                    .filter(task -> task.getUser() != null
                            && task.getUser().getEmail() != null
                            && task.getUser().getEmail().equalsIgnoreCase(email))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.save(task));
    }
}
