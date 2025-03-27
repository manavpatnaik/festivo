package com.manav.festivo.service;

import com.manav.festivo.dto.ServiceProviderPatchDTO;
import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceProviderService {
    private final ServiceProviderRepository serviceProviderRepository;

    @Transactional
    public ServiceProvider createServiceProvider(ServiceProvider serviceProvider) {
        if (serviceProvider.getUser() != null &&
        serviceProviderRepository.findByUserId(serviceProvider.getUser().getId()) != null) {
            throw new IllegalArgumentException("Service provider already exists for this user");
        }
        return serviceProviderRepository.save(serviceProvider);
    }

    public Optional<ServiceProvider> getServiceProviderById(UUID id) {
        return serviceProviderRepository.findById(id);
    }

    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepository.findAll();
    }

    public List<ServiceProvider> getServiceProvidersByServiceType(ServiceProvider.ServiceType serviceType) {
        return serviceProviderRepository.findByServiceType(serviceType);
    }

    public List<ServiceProvider> getServiceProvidersByCity(String city) {
        return serviceProviderRepository.findByCity(city);
    }

    public ServiceProvider getServiceProviderByUserId(UUID userId) {
        return serviceProviderRepository.findByUserId(userId);
    }

    @Transactional
    public ServiceProvider updateServiceProvider(ServiceProvider serviceProvider) {
        if (!serviceProviderRepository.existsById(serviceProvider.getId())) {
            throw new RuntimeException("Service provider not found");
        }
        return serviceProviderRepository.save(serviceProvider);
    }

    @Transactional
    public void deleteServiceProvider(UUID id) {
        ServiceProvider provider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        System.out.println("Deleting service provider " + id);
        provider.setUser(null); // Clear the relationship
        serviceProviderRepository.delete(provider);
    }

    @Transactional
    public ServiceProvider patchServiceProvider(UUID id, ServiceProviderPatchDTO serviceProviderPatchDTO) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).orElseThrow(() -> new RuntimeException("Service provider not found"));
        serviceProviderPatchDTO.applyTo(serviceProvider);
        return serviceProviderRepository.save(serviceProvider);
    }
}
