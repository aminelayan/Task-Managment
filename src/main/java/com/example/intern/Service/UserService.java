package com.example.intern.Service;

import com.example.intern.Models.User;
import com.example.intern.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> searchOnName(String term) {
        List<User> searchterm = userRepository.findByFirstNameContainingOrLastNameContaining(term, term);
        return searchterm;
    }
}