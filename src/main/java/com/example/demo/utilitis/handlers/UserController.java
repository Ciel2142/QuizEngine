package com.example.demo.utilitis.handlers;

import com.example.demo.utilitis.models.User;
import com.example.demo.utilitis.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User is already registered!")
    static class UsernameIsTakenException extends RuntimeException {
    }

    @PostMapping(value = "/api/register", consumes = "application/json")
    public String registerUser(@Valid @RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent())
            throw new UsernameIsTakenException();
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        return String.format("Email %s registration successful", userRepository.save(newUser).getEmail());
    }
}