package com.manav.festivo.controller;

import com.manav.festivo.dto.ServiceProviderPricingCreationDTO;
import com.manav.festivo.model.ServiceProviderPricing;
import com.manav.festivo.service.ServiceProviderPricingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-providers/{serviceProviderId}/pricing")
@RequiredArgsConstructor
public class ServiceProviderPricingController {
    private final ServiceProviderPricingService pricingService;

    @PostMapping
    public ResponseEntity<ServiceProviderPricing> createPricing(
            @PathVariable UUID serviceProviderId,
            @Valid @RequestBody ServiceProviderPricingCreationDTO creationDTO) {
        ServiceProviderPricing pricing = creationDTO.toServiceProviderPricing();
        ServiceProviderPricing createdPricing = pricingService.createPricing(serviceProviderId, pricing);
        return new ResponseEntity<>(createdPricing, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderPricing> getPricingById(@PathVariable UUID id) {
        ServiceProviderPricing pricing = pricingService.getPricingById(id);
        return ResponseEntity.ok(pricing);
    }

    @GetMapping
    public ResponseEntity<List<ServiceProviderPricing>> getPricingByServiceProvider(
            @PathVariable UUID serviceProviderId) {
        List<ServiceProviderPricing> pricingList = pricingService.getPricingByServiceProvider(serviceProviderId);
        return ResponseEntity.ok(pricingList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceProviderPricing> updatePricing(
            @PathVariable UUID id,
            @Valid @RequestBody ServiceProviderPricing pricing) {
        ServiceProviderPricing updatedPricing = pricingService.updatePricing(id, pricing);
        return ResponseEntity.ok(updatedPricing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable UUID id) {
        pricingService.deletePricing(id);
        return ResponseEntity.noContent().build();
    }
}
