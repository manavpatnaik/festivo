package com.manav.festivo.controller;

import com.manav.festivo.dto.ServiceProviderAvailabilityCreationDTO;
import com.manav.festivo.model.ServiceProviderAvailability;
import com.manav.festivo.service.ServiceProviderAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-providers/{serviceProviderId}/availability")
@RequiredArgsConstructor
public class ServiceProviderAvailabilityController {
    private final ServiceProviderAvailabilityService availabilityService;

    @PostMapping
    public ResponseEntity<ServiceProviderAvailability> createAvailability(
            @PathVariable UUID serviceProviderId,
            @Valid @RequestBody ServiceProviderAvailabilityCreationDTO creationDTO) {
        ServiceProviderAvailability availability = creationDTO.toServiceProviderAvailability();
        ServiceProviderAvailability createdAvailability = availabilityService.createAvailability(serviceProviderId, availability);
        return new ResponseEntity<>(createdAvailability, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderAvailability> getAvailabilityById(@PathVariable UUID id) {
        ServiceProviderAvailability availability = availabilityService.getAvailabilityById(id);
        return ResponseEntity.ok(availability);
    }

    @GetMapping
    public ResponseEntity<List<ServiceProviderAvailability>> getAvailabilityByServiceProvider(
            @PathVariable UUID serviceProviderId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return ResponseEntity.ok(availabilityService.getAvailabilityByServiceProviderAndDateRange(serviceProviderId, startDate, endDate));
        }
        return ResponseEntity.ok(availabilityService.getAvailabilityByServiceProvider(serviceProviderId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceProviderAvailability> updateAvailability(
            @PathVariable UUID id,
            @Valid @RequestBody ServiceProviderAvailability availability) {
        ServiceProviderAvailability updatedAvailability = availabilityService.updateAvailability(id, availability);
        return ResponseEntity.ok(updatedAvailability);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable UUID id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}
