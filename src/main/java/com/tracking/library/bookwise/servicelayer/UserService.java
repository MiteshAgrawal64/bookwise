package com.tracking.library.bookwise.servicelayer;

import com.tracking.library.bookwise.entities.User;
import com.tracking.library.bookwise.entities.enums.Role;
import com.tracking.library.bookwise.repositories.UserRepository;
import com.tracking.library.bookwise.servicelayer.interfaces.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Primary
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Override
    public User createUser(User user) {
        logger.info("Attempting to create user with username: {}", user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Username '{}' already exists", user.getUsername());
            throw new RuntimeException("Username already exists");
        }

        user.setRole(Role.MEMBER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        logger.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);

        return userRepository.findById(id)
                .map(user -> {
                    logger.info("User found with ID: {}", id);
                    return user;
                })
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new RuntimeException("User not found");
                });
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        logger.info("Fetched all users, total count: {}", users.size());
        return users;
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
        logger.info("User with ID: {} deleted successfully", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.info("User loaded successfully: {}", username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
