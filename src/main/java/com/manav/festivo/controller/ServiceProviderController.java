package com.manav.festivo.controller;

import com.manav.festivo.dto.ServiceProviderCreationDTO;
import com.manav.festivo.dto.ServiceProviderPatchDTO;
import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.model.User;
import com.manav.festivo.repository.ServiceProviderRepository;
import com.manav.festivo.service.ServiceProviderService;
import com.manav.festivo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-providers")
public class ServiceProviderController {
    private final ServiceProviderService serviceProviderService;
    private final UserService userService;
    private final ServiceProviderRepository serviceProviderRepository;

    @PostMapping
    public ResponseEntity<ServiceProvider> createServiceProvider(@Valid @RequestBody ServiceProviderCreationDTO serviceProviderCreationDTO) {
        User user = userService.getUserById(serviceProviderCreationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ServiceProvider serviceProvider = serviceProviderCreationDTO.toServiceProvider(user);
        ServiceProvider createdServiceProvider = serviceProviderService.createServiceProvider(serviceProvider);
        return new ResponseEntity<>(createdServiceProvider, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProvider> getServiceProviderById(@PathVariable UUID id) {
        return serviceProviderService.getServiceProviderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ServiceProvider>> getAllServiceProviders() {
        return ResponseEntity.ok(serviceProviderService.getAllServiceProviders());
    }

    @GetMapping("/type/{serviceType}")
    public ResponseEntity<List<ServiceProvider>> getServiceProvidersByServiceType(@PathVariable String serviceType) {
        ServiceProvider.ServiceType type;
        try {
            type = ServiceProvider.ServiceType.valueOf(serviceType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid service type");
        }
        return ResponseEntity.ok(serviceProviderService.getServiceProvidersByServiceType(type));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<ServiceProvider>> getServiceProvidersByCity(@PathVariable String city) {
        return ResponseEntity.ok(serviceProviderService.getServiceProvidersByCity(city));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceProvider> updateServiceProvider(@PathVariable UUID id,
                                                                 @Valid @RequestBody ServiceProvider serviceProvider) {
        serviceProvider.setId(id);
        ServiceProvider updatedServiceProvider = serviceProviderService.updateServiceProvider(serviceProvider);
        return new ResponseEntity<>(updatedServiceProvider, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable UUID id) {
        serviceProviderService.deleteServiceProvider(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceProvider> patchServiceProvider(
            @PathVariable UUID id,
            @Valid @RequestBody ServiceProviderPatchDTO serviceProviderPatchDTO
    ) {
        ServiceProvider updatedServiceProvider = serviceProviderService.patchServiceProvider(id, serviceProviderPatchDTO);
        return new ResponseEntity<>(updatedServiceProvider, HttpStatus.OK);
    }
}
