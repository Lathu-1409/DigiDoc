package com.docmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.docmanager.model.User;
import com.docmanager.service.UserService;
import com.docmanager.exception.AuthenticationFailedException;
import com.docmanager.exception.UserNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Handles user registration.
     *
     * @param user User object containing registration details.
     * @return ResponseEntity with registered user object and HTTP status.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to register user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles user login.
     *
     * @param credentials User object containing login details (email and password).
     * @return ResponseEntity with authenticated user object and HTTP status.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User credentials) {
        try {
            User authenticatedUser = userService.authenticateUser(credentials.getEmail(), credentials.getPassword());
            return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
        } catch (AuthenticationFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred during login: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Fetch user by ID.
     *
     * @param id User ID to fetch details.
     * @return ResponseEntity with user object if found or error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            User user = userService.getUserById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the user: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}