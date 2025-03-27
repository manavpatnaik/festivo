package com.manav.festivo.repository;

import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.model.ServiceProviderPricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceProviderPricingRepository extends JpaRepository<ServiceProviderPricing, UUID> {
    List<ServiceProviderPricing> findByServiceProvider(ServiceProvider serviceProvider);
}
