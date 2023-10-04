package com.example.intern.Controller;

import com.example.intern.Models.User;
import com.example.intern.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-list";
    }

    @GetMapping("/{userId}")
    public String getUserDetails(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        model.addAttribute("user", user);
        return "user-details";
    }
    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("newUser", new User());
        return "create-user";
    }
    @PostMapping
    public String createUser(@Valid @ModelAttribute("newUser") User newUser) {
        try {
            User userCreated = userRepository.save(newUser);
            // You can optionally redirect to the user details page or any other page after successful creation.
            return "redirect:/users/" + userCreated.getId();
        } catch (Exception e) {
            // Handle the error and display a message if needed
            return "create-user"; // Return to the create user form with an error message
        }
    }
    @GetMapping("/{userId}/edit")
    public String showUpdateUserForm(@Valid @PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        model.addAttribute("updatedUser", user);
        return "update-user";
    }

    @PostMapping("/{userId}")
    public String updateUser(
            @PathVariable Long userId,
            @Valid @ModelAttribute("updatedUser") User updatedUser
    ) {
        try {
            if (!userRepository.existsById(userId)) {
                return "redirect:/users";
            }
            updatedUser.setId(userId);
            userRepository.save(updatedUser);
            return "redirect:/users/" + userId ;
        } catch (Exception e) {
            return "update-user";
        }
    }
    @GetMapping("/{userId}/delete")
    public String showDeleteUserConfirmation(@PathVariable Long userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        model.addAttribute("user", user);
        return "delete-user";
    }
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        try {
            if (!userRepository.existsById(userId)) {
                return "redirect:/users";
            }
            userRepository.deleteById(userId);
            return "redirect:/users";
        } catch (Exception e) {
            return "delete-user";
        }
    }
}
