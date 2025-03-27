package com.manav.festivo.dto;

import com.manav.festivo.model.ServicePortfolio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServicePortfolioCreationDTO {
    @NotBlank(message = "Event type is required")
    private String eventType;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

//    private List<String> imageUrls = new ArrayList<>();

    public ServicePortfolio toServicePortfolio() {
        ServicePortfolio portfolio = new ServicePortfolio();
        portfolio.setEventType(ServicePortfolio.EventType.valueOf(eventType.toUpperCase()));
        portfolio.setDescription(description);
//        portfolio.setImageUrls(imageUrls);
        return portfolio;
    }
}
