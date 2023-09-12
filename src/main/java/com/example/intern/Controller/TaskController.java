package com.example.intern.Controller;

import com.example.intern.Models.Task;
import com.example.intern.Models.User;
import com.example.intern.Repository.TaskRepository;
import com.example.intern.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;


    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task) {
        try {
            Task createdTask = taskRepository.save(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid task data");
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@Valid @PathVariable Long taskId, @RequestBody Task updatedTask) {
        try {
            if (!taskRepository.existsById(taskId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            }
            updatedTask.setId(taskId);
            Task savedTask = taskRepository.save(updatedTask);
            return ResponseEntity.ok(savedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update task");
        }
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        try {
            if (!taskRepository.existsById(taskId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            }

            taskRepository.deleteById(taskId);

            return ResponseEntity.status(HttpStatus.OK).body("Task Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete task");
        }
    }

    @PostMapping("/{taskId}/assign")
    public ResponseEntity<?> assignUserToTask(@PathVariable Long taskId, @RequestParam Long userId) {
        taskService.assignUserToTask(taskId, userId);
        return ResponseEntity.ok("User assigned to the task successfully");
    }

    @GetMapping("/{taskId}/assigned-users")
    public ResponseEntity<List<User>> getAssignedUsersForTask(@PathVariable Long taskId) {
        List<User> assignedUsers = taskService.getAssignedUsersForTask(taskId);
        return ResponseEntity.ok(assignedUsers);
    }

    @DeleteMapping("/{taskId}/remove/{userId}")
    public ResponseEntity<?> removeUserFromTask(@PathVariable Long taskId, @PathVariable Long userId) {
        taskService.removeUserFromTask(taskId, userId);
        return ResponseEntity.ok("User removed from the task successfully");
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Task>> getTasksSortedByDueDate(
            @RequestParam(required = true) String by,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(defaultValue= "true") boolean completed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Task> sortedTasks;
//        Page<Task> paginatedTasks;

        switch (by) {
            case "dueDate":
                sortedTasks = taskService.sortedByDueDate(order);
                break;
            case "title":
                sortedTasks = taskService.sortedByTitle(order);
                break;
            case "completed":
                sortedTasks=taskService.filterByCompletion(completed);
                break;
//            case "pagination":
//                paginatedTasks= taskService.paginatedTasks(page, size);
//                break;
            default:
                return ResponseEntity.badRequest().body(Collections.emptyList());
        }

            return ResponseEntity.ok(sortedTasks);
    }
    @GetMapping("/duedate")
    public ResponseEntity<List<Task>> getTasksFilteredByDueDate(
            @RequestParam  LocalDate from,
            @RequestParam  LocalDate to
    ) {
        List<Task> filteredTasks = taskService.filterByDueDate(from, to);

        if (filteredTasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(filteredTasks);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Task>> getPaginatedTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Task> paginatedTasks = taskService.paginatedTasks(page, size);

        if (paginatedTasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(paginatedTasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchUsers(@RequestParam(required = true) String searchTerm){
        List<Task> searchResult = taskService.searchByTerm(searchTerm);
        if (searchResult.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(searchResult);
    }

}

