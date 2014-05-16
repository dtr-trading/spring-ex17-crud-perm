package com.dtr.oas.dao;

import java.util.List;

import com.dtr.oas.exception.DuplicateUserException;
import com.dtr.oas.exception.UserNotFoundException;
import com.dtr.oas.model.User;

public interface UserDAO {

    public void addUser(User user) throws DuplicateUserException;

    public User getUser(int userId) throws UserNotFoundException;
    
    public User getUser(String username) throws UserNotFoundException;

    public void updateUser(User user) throws UserNotFoundException, DuplicateUserException;

    public void deleteUser(int userId) throws UserNotFoundException;

    public List<User> getUsers();

}
