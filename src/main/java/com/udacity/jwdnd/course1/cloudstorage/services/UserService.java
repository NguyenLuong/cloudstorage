package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private HashService hashService;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public int createUser(User user) {
        logger.debug("Start create user");
        System.out.println("start create user");
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        return mapper.insertUser(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    public User getUser(String username) {
        return mapper.getUser(username);
    }

    public boolean isUsernameAvailable(String username) {
        return mapper.getUser(username) != null;
    }
}
