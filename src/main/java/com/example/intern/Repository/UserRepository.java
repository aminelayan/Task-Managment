package com.example.intern.Repository;

import com.example.intern.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByFirstNameContainingOrLastNameContaining(String firstName,String lastName);
}
