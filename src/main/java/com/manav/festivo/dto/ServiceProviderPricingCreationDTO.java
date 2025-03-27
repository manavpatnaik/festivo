package com.manav.festivo.dto;

import com.manav.festivo.model.PricingTier;
import com.manav.festivo.model.ServiceProviderPricing;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceProviderPricingCreationDTO {
    @NotBlank(message = "Event type is required")
    private String eventType;

    private List<PricingTier> pricingTiers = new ArrayList<>();

    public ServiceProviderPricing toServiceProviderPricing() {
        ServiceProviderPricing pricing = new ServiceProviderPricing();
        pricing.setEventType(ServiceProviderPricing.EventType.valueOf(eventType.toUpperCase()));
        pricing.setPricingTiers(pricingTiers);
        return pricing;
    }
}
