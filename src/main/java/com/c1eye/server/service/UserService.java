package com.c1eye.server.service;

import com.c1eye.server.model.User;
import com.c1eye.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author c1eye
 * time 2021/10/18 21:58
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User getUserById(Long id){
        return userRepository.findFirstById(id);
    }
}
