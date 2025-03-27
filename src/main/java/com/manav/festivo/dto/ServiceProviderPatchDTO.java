package com.manav.festivo.dto;

import com.manav.festivo.model.ServiceProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceProviderPatchDTO {
    private String serviceType;
    @Size(max = 100, message = "Company name must be less than 100 characters")
    private String companyName;
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private double averageRating;
    private boolean isVerified;

    public void applyTo(ServiceProvider serviceProvider) {
        if (companyName != null) serviceProvider.setCompanyName(companyName);
        if (description != null) serviceProvider.setDescription(description);
        if (address != null) serviceProvider.setAddress(address);
        if (city != null) serviceProvider.setCity(city);
        if (state != null) serviceProvider.setState(state);
        if (country != null) serviceProvider.setCountry(country);
        if (pinCode != null) serviceProvider.setPinCode(pinCode);
        if (averageRating != 0) serviceProvider.setAverageRating(averageRating);
        if (isVerified) serviceProvider.setVerified(true);
        if (serviceType != null) {
            try {
                serviceProvider.setServiceType(ServiceProvider.ServiceType.valueOf(serviceType.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid service type: " + serviceType);
            }
        }
    }
}
