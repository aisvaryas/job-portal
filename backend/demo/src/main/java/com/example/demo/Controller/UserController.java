package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;
import com.example.demo.dto.UserRegisterdto;
import com.example.demo.dto.UserResponsedto;
import com.example.demo.mapper.UserMapper;
import com.example.demo.response.Apiresponse;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<Apiresponse<UserResponsedto>> register(@Valid @RequestBody UserRegisterdto dto) {
        User user = service.saveUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Apiresponse<>("User Registered Successfully", 201, UserMapper.toDto(user)));
    }

    @GetMapping("/all")
    public ResponseEntity<Apiresponse<List<User>>> all() {
        List<User> list = service.getAllUsers();
        if(list.isEmpty()) throw new com.example.demo.exception.ResourceNotFoundException("No Users Found");
        return ResponseEntity.ok(new Apiresponse<>("Users Fetched Successfully",200,list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apiresponse<User>> byId(@PathVariable Long id) {
        User user = service.getUserById(id);
        if(user==null) throw new com.example.demo.exception.ResourceNotFoundException("User Not Found");
        return ResponseEntity.ok(new Apiresponse<>("User Found",200,user));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<Apiresponse<String>> deactivate(@PathVariable Long id) {
        String msg = service.deleteUser(id);
        Apiresponse<String> res = new Apiresponse<>(msg, 200, msg);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/reactivate/{id}")
    public ResponseEntity<Apiresponse<String>> reactivate(@PathVariable Long id) {
        String msg = service.reactivateUser(id);
        Apiresponse<String> res = new Apiresponse<>(msg,200,msg);
        return ResponseEntity.ok(res);
    }
}