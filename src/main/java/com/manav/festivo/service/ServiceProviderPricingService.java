package com.manav.festivo.service;

import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.model.ServiceProviderPricing;
import com.manav.festivo.repository.ServiceProviderPricingRepository;
import com.manav.festivo.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProviderPricingService {
    private final ServiceProviderPricingRepository pricingRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Transactional
    public ServiceProviderPricing createPricing(UUID serviceProviderId, ServiceProviderPricing pricing) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        pricing.setServiceProvider(provider);
        return pricingRepository.save(pricing);
    }

    public ServiceProviderPricing getPricingById(UUID id) {
        return pricingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing not found"));
    }

    public List<ServiceProviderPricing> getPricingByServiceProvider(UUID serviceProviderId) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        return pricingRepository.findByServiceProvider(provider);
    }

    @Transactional
    public ServiceProviderPricing updatePricing(UUID id, ServiceProviderPricing updatedPricing) {
        ServiceProviderPricing existingPricing = pricingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing not found"));

        // Update fields from the request
        if (updatedPricing.getEventType() != null) {
            existingPricing.setEventType(updatedPricing.getEventType());
        }
        if (updatedPricing.getPricingTiers() != null) {
            existingPricing.setPricingTiers(updatedPricing.getPricingTiers());
        }
        return pricingRepository.save(existingPricing);
    }

    @Transactional
    public void deletePricing(UUID id) {
        if (!pricingRepository.existsById(id)) {
            throw new RuntimeException("Pricing not found");
        }
        pricingRepository.deleteById(id);
    }
}
