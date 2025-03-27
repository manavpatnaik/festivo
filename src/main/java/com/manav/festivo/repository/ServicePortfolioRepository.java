package com.manav.festivo.repository;

import com.manav.festivo.model.ServicePortfolio;
import com.manav.festivo.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface ServicePortfolioRepository extends JpaRepository<ServicePortfolio, UUID> {
    List<ServicePortfolio> findByServiceProvider(ServiceProvider serviceProvider);
    List<ServicePortfolio> findByServiceProviderAndEventType(ServiceProvider serviceProvider, ServicePortfolio.EventType eventType);
}
