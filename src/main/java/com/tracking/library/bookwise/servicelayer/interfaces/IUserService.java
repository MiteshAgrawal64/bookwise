package com.tracking.library.bookwise.servicelayer.interfaces;

import com.tracking.library.bookwise.entities.User;

import java.util.List;

public interface IUserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);
}
