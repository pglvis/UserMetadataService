package com.example.usermetadata.user.controller;


import com.example.usermetadata.user.dto.UserDto;
import com.example.usermetadata.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;


@RestController
@RequestMapping("/users")
public class UserController{

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@RequestHeader("Idempotency-Key") String key,  @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto, key);
        return ResponseEntity
                .created(URI.create("/user/" + user.getUserId()))
                .body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        long userId = Long.parseLong(id);
        UserDto user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }
}
