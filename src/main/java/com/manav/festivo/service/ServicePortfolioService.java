package com.manav.festivo.service;

import com.manav.festivo.model.ServicePortfolio;
import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.repository.ServicePortfolioRepository;
import com.manav.festivo.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicePortfolioService {
    private final ServicePortfolioRepository portfolioRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Transactional
    public ServicePortfolio createPortfolio(UUID serviceProviderId, ServicePortfolio portfolio) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        portfolio.setServiceProvider(provider);
        return portfolioRepository.save(portfolio);
    }

    public ServicePortfolio getPortfolioById(UUID id) {
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }

    public List<ServicePortfolio> getPortfolioByServiceProvider(UUID serviceProviderId) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        return portfolioRepository.findByServiceProvider(provider);
    }

    @Transactional
    public ServicePortfolio updatePortfolio(UUID id, ServicePortfolio portfolio) {
        ServicePortfolio existingPortfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        if (portfolio.getEventType() != null)
            existingPortfolio.setEventType(portfolio.getEventType());
        if (portfolio.getDescription() != null)
            existingPortfolio.setDescription(portfolio.getDescription());
        if (portfolio.getImageUrls() != null)
            existingPortfolio.setImageUrls(portfolio.getImageUrls());
        return portfolioRepository.save(existingPortfolio);
    }

    @Transactional
    public void deletePortfolio(UUID id) {
        if (!portfolioRepository.existsById(id)) {
            throw new RuntimeException("Portfolio not found");
        }
        portfolioRepository.deleteById(id);
    }

    public List<ServicePortfolio> getPortfolioByServiceProviderAndEventType(UUID serviceProviderId, ServicePortfolio.EventType eventType) {
        ServiceProvider provider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));
        return portfolioRepository.findByServiceProviderAndEventType(provider, eventType);
    }
}
