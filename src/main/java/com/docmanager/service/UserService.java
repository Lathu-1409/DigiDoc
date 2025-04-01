package com.docmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docmanager.model.User;
import com.docmanager.repository.UserRepository;
import com.docmanager.exception.UserNotFoundException;
import com.docmanager.exception.AuthenticationFailedException;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user.
     *
     * @param user The user object to be registered.
     * @return The saved User object.
     */
    public User registerUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register user: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user with email and password.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return The authenticated User object.
     * @throws AuthenticationFailedException if authentication fails.
     */
    public User authenticateUser(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new AuthenticationFailedException("No user found with the provided email.");
            }
            if (!user.getPassword().equals(password)) { // TODO: Replace with proper password hashing and verification
                throw new AuthenticationFailedException("Incorrect password.");
            }
            return user;
        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during authentication: " + e.getMessage());
        }
    }

    /**
     * Fetches a user by ID.
     *
     * @param id The user ID.
     * @return An Optional containing the User object if found.
     * @throws UserNotFoundException if the user does not exist.
     */
    public Optional<User> getUserById(String id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                throw new UserNotFoundException("No user found with ID: " + id);
            }
            return user;
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching the user: " + e.getMessage());
        }
    }
}