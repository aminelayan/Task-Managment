package com.example.intern.Service;

import com.example.intern.Models.Task;
import com.example.intern.Models.User;
import com.example.intern.Repository.TaskRepository;
import com.example.intern.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService  {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public void assignUserToTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if the user is already assigned to the task
        if (task.getAssignedUsers().contains(user)) {
            throw new IllegalStateException("User is already assigned to the task");
        }

        task.getAssignedUsers().add(user);
        taskRepository.save(task);
    }
    public List<User> getAssignedUsersForTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        return new ArrayList<>(task.getAssignedUsers());
    }
    public void removeUserFromTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if the user is assigned to the task
        if (!task.getAssignedUsers().contains(user)) {
            throw new IllegalStateException("User is not assigned to the task");
        }

        task.getAssignedUsers().remove(user);
        taskRepository.save(task);
    }
    public List<Task> sortedBy(String sortOrder,String properties) {
        Sort.Direction direction = Sort.Direction.ASC;

        if ("desc".equalsIgnoreCase(sortOrder)) {
            direction = Sort.Direction.DESC;
        }
        return taskRepository.findAll(Sort.by(direction,properties));
    }
    public List<Task> filterByDueDate(LocalDate fromDate, LocalDate toDate) {
        return taskRepository.findByDueDateBetween(fromDate, toDate);
    }

    public List<Task> filterByCompletion(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    public Page<Task> paginatedTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable);
    }
    public List<Task> searchByTerm(String searchTerm){
        return taskRepository.findByUserFirstNameOrUserLastName(searchTerm,searchTerm);
    }


}
