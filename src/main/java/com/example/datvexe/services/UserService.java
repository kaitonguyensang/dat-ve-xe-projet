package com.example.datvexe.services;

import com.example.datvexe.models.User;
import com.example.datvexe.payloads.requests.UserRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    User getUserById(Long userId);
    List<User> getAll();
    DataResponse updateUser(UserRequest userRequest, Long id);
}
