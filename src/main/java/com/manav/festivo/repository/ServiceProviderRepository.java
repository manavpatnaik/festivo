package com.manav.festivo.repository;

import com.manav.festivo.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, UUID> {
    List<ServiceProvider> findByServiceType(ServiceProvider.ServiceType serviceType);
    List<ServiceProvider> findByCity(String city);
    ServiceProvider findByUserId(UUID userId);
}
