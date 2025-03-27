package com.manav.festivo.service;

import com.manav.festivo.dto.UserLoginDTO;
import com.manav.festivo.dto.UserPatchDTO;
import com.manav.festivo.model.User;
import com.manav.festivo.repository.UserRepository;
import com.manav.festivo.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public String login(UserLoginDTO loginDTO) {
        // Find the user by email
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> {
                    return new RuntimeException("Invalid email or password");
                });

        // Verify the password
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return token;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User not found");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public User patchUser(UUID id, UserPatchDTO patchDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Selectively update non-null fields
        if (patchDTO.getFirstName() != null) {
            existingUser.setFirstName(patchDTO.getFirstName());
        }
        if (patchDTO.getLastName() != null) {
            existingUser.setLastName(patchDTO.getLastName());
        }
        if (patchDTO.getEmail() != null) {
            // Check for email uniqueness if changing email
            if (!existingUser.getEmail().equals(patchDTO.getEmail()) &&
                    userRepository.findByEmail(patchDTO.getEmail()).isPresent()) {
                throw new RuntimeException("Email already in use");
            }
            existingUser.setEmail(patchDTO.getEmail());
        }
        if (patchDTO.getRole() != null) {
            existingUser.setRole(patchDTO.getRole());
        }
        if (patchDTO.getIsActive() != null) {
            existingUser.setActive(patchDTO.getIsActive());
        }

        return userRepository.save(existingUser);
    }
}
