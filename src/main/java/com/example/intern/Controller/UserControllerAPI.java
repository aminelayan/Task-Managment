package com.example.intern.Controller;

import com.example.intern.Models.User;
import com.example.intern.Repository.UserRepository;
import com.example.intern.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/users")

public class UserControllerAPI {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String input) {
        List<User> searchResult;

        if (input != null && !input.trim().isEmpty()) {
            // If input parameter is provided, perform a search
            searchResult = userService.searchOnName(input);
        } else {
            // If input parameter is not provided, get all users
            searchResult = userRepository.findAll();
        }

        if (searchResult.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(searchResult);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserByID(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Get the User");
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User newUser) {
        try {
            User usercreated = userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(usercreated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Create New User");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@Valid @PathVariable Long userId,@RequestBody User updateduser){
        try{
            if(!userRepository.existsById(userId)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }
            updateduser.setId(userId);
            User updateUser = userRepository.save(updateduser);
            return ResponseEntity.ok(updateUser);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Update The User");
        }
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId){
        try{
            if(!userRepository.existsById(userId)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
            }
            userRepository.deleteById(userId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Delete The User");
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = true) String searchTerm){
        List<User> searchResult = userService.searchOnName(searchTerm);
        if (searchResult.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(searchResult);
    }

}
