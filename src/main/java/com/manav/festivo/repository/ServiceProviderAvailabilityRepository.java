package com.manav.festivo.repository;

import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.model.ServiceProviderAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ServiceProviderAvailabilityRepository extends JpaRepository<ServiceProviderAvailability, UUID> {
    List<ServiceProviderAvailability> findByServiceProvider(ServiceProvider serviceProvider);

    List<ServiceProviderAvailability> findByServiceProviderAndAvailabilityDateBetween(
            ServiceProvider serviceProvider, LocalDate startDate, LocalDate endDate);
}
