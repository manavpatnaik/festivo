package com.manav.festivo.controller;

import com.manav.festivo.dto.UserCreationDTO;
import com.manav.festivo.dto.UserLoginDTO;
import com.manav.festivo.dto.UserPatchDTO;
import com.manav.festivo.model.User;
import com.manav.festivo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        User createdUser = userService.createUser(userCreationDTO.toUser());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        System.out.println("UserController.signUp: " + userCreationDTO);
        User user = userCreationDTO.toUser();
        user = userService.createUser(user);
        System.out.println("UserController.signUp: " + user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String token = userService.login(loginDTO);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable UUID id, @Valid @RequestBody UserPatchDTO patchDTO) {
        User updatedUser = userService.patchUser(id, patchDTO);
        return ResponseEntity.ok(updatedUser);
    }


}
