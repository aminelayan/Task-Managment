package com.example.intern.Repository;

import com.example.intern.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByDueDateBetween(LocalDate fromDate, LocalDate toDate);

    List<Task> findByCompleted(boolean completed);


    @Query("SELECT DISTINCT t FROM Task t JOIN t.assignedUsers u " +
            "WHERE u.firstName = :firstName OR u.lastName = :lastName")
    List<Task> findByUserFirstNameOrUserLastName(@Param("firstName") String firstName,
                                                 @Param("lastName") String lastName);
}