package com.example.intern.Repository;

import com.example.intern.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task>findByDueDateBetween(LocalDate fromDate, LocalDate toDate);
    List<Task> findByCompleted(boolean completed);
    List<Task> findByTitleContainingOrDescriptionContaining(String Title,String Descrp);

}