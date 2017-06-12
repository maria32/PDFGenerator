package com.PDF.service.impl;

import com.PDF.exception.ResourceAlreadyExistsException;
import com.PDF.exception.ResourceNotFoundException;
import com.PDF.model.User;
import com.PDF.repository.UserRepository;
import com.PDF.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by martanase on 6/7/2017.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User create(User user) {
        User existingUsermail = user.getEmail() == null ? null : userRepository.findByEmail(user.getEmail());
        User existingUsername = user.getUsername() == null ? null : userRepository.findByUsername(user.getUsername());
        if (existingUsermail != null || existingUsername != null)
            throw new ResourceAlreadyExistsException();
        return userRepository.save(user);
    }

    @Override
    public User getOne(Long id) {
        User user = id == null ? null : userRepository.findOne(id);
        if(user == null)
            throw new ResourceNotFoundException();
        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(userRepository.findOne(id));
    }

    @Override
    public User checkCredentials(User user) {
        User userByUsername;
        if(user != null && user.getUsername() != null && user.getPassword() != null){
            userByUsername = userRepository.findByUsername(user.getUsername());
            if(userByUsername != null && userByUsername.getPassword().equals(user.getPassword()))
                return userByUsername;
        }
        return null;
    }
}
