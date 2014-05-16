package com.dtr.oas.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dtr.oas.exception.DuplicateUserException;
import com.dtr.oas.exception.UserNotFoundException;
import com.dtr.oas.model.User;

@Repository
public class UserDAOImpl implements UserDAO {
	static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

	@Override
	public void addUser(User user) throws DuplicateUserException {
        logger.debug("UserDAOImpl.addUser() - [" + user.getUsername() + "]");
        try {
            // if the user is not found, then a UserNotFoundException is
            // thrown from the getUser method call, and the new user will be
            // added.
            //
            // if the user is found, then the flow will continue from the getUser
            // method call and the DuplicateUserException will be thrown.
            User userCheck = getUser(user.getUsername());
            String message = "The user [" + userCheck.getUsername() + "] already exists";
            throw new DuplicateUserException(message);
        } catch (UserNotFoundException e) {
            getCurrentSession().save(user);
        }
	}

    @Override
    public User getUser(int userId) throws UserNotFoundException {
        logger.debug("UserDAOImpl.getUser() - [" + userId + "]");
        User userObject = (User) getCurrentSession().get(User.class, userId);

        if (userObject == null) {
            throw new UserNotFoundException("User id [" + userId + "] not found");
        } else {
            return userObject;
        }
    }

	@SuppressWarnings("unchecked")
	@Override
	public User getUser(String usersName) throws UserNotFoundException {
		logger.debug("UserDAOImpl.getUser() - [" + usersName + "]");
		Query query = getCurrentSession().createQuery("from User where username = :usersName ");
		query.setString("usersName", usersName);

		logger.debug(query.toString());
		if (query.list().size() == 0 ) {
			throw new UserNotFoundException("User [" + usersName + "] not found");
		} else {
			logger.debug("User List Size: " + query.list().size());
			List<User> list = (List<User>)query.list();
	        User userObject = (User) list.get(0);

	        return userObject;
		}
	}

	@Override
	public void updateUser(User user) throws UserNotFoundException, DuplicateUserException {
	    User userToUpdate = getUser(user.getId());
	    
	    try {
	        User userCheck = getUser(user.getUsername());
	        if (userCheck.getId() == userToUpdate.getId()) {
	            userToUpdate.setEnabled(user.getEnabled());
	            userToUpdate.setPassword(user.getPassword());
	            userToUpdate.setUsername(user.getUsername());
	            userToUpdate.setRole(user.getRole());
	            getCurrentSession().update(userToUpdate);
	        } else {
	            String message = "The user [" + userCheck.getUsername() + "] already exists";
	            throw new DuplicateUserException(message);
	        }
	    } catch (UserNotFoundException e) {
	        userToUpdate.setEnabled(user.getEnabled());
	        userToUpdate.setPassword(user.getPassword());
	        userToUpdate.setUsername(user.getUsername());
	        userToUpdate.setRole(user.getRole());
            getCurrentSession().update(userToUpdate);
	    }
	}

	@Override
	public void deleteUser(int userId) throws UserNotFoundException {
        User user = getUser(userId);
        if (user != null) {
            getCurrentSession().delete(user);
        }
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        String hql = "FROM User u ORDER BY u.id";
        return getCurrentSession().createQuery(hql).list();
    }
}
