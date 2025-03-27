package com.manav.festivo.service;

import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.model.ServiceProviderAvailability;
import com.manav.festivo.repository.ServiceProviderAvailabilityRepository;
import com.manav.festivo.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProviderAvailabilityService {
    private final ServiceProviderAvailabilityRepository availabilityRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Transactional
    public ServiceProviderAvailability createAvailability(UUID serviceProviderId, ServiceProviderAvailability availability) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        availability.setServiceProvider(provider);
        return availabilityRepository.save(availability);
    }

    public ServiceProviderAvailability getAvailabilityById(UUID id) {
        return availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
    }

    public List<ServiceProviderAvailability> getAvailabilityByServiceProvider(UUID serviceProviderId) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        return availabilityRepository.findByServiceProvider(provider);
    }

    public List<ServiceProviderAvailability> getAvailabilityByServiceProviderAndDateRange(
            UUID serviceProviderId, LocalDate startDate, LocalDate endDate) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        return availabilityRepository.findByServiceProviderAndAvailabilityDateBetween(provider, startDate, endDate);
    }

    @Transactional
    public ServiceProviderAvailability updateAvailability(UUID id, ServiceProviderAvailability availability) {
        ServiceProviderAvailability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        if (availability.getAvailabilityDate() != null)
            existingAvailability.setAvailabilityDate(availability.getAvailabilityDate());
        if (availability.getStatus() != null)
           existingAvailability.setStatus(availability.getStatus());
        return availabilityRepository.save(existingAvailability);
    }

    @Transactional
    public void deleteAvailability(UUID id) {
        if (!availabilityRepository.existsById(id)) {
            throw new RuntimeException("Availability not found");
        }
        availabilityRepository.deleteById(id);
    }
}
