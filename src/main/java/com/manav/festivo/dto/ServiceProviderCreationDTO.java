package com.manav.festivo.dto;

import com.manav.festivo.model.ServiceProvider;
import com.manav.festivo.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ServiceProviderCreationDTO {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Service type is required")
    private String serviceType;

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name must be less than 100 characters")
    private String companyName;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Pin code is required")
    private String pinCode;

    public ServiceProvider toServiceProvider(User user) {
        return ServiceProvider.builder()
                .user(user)
                .serviceType(ServiceProvider.ServiceType.valueOf(serviceType.toUpperCase()))
                .companyName(companyName)
                .description(description)
                .address(address)
                .city(city)
                .state(state)
                .country(country)
                .pinCode(pinCode)
                .averageRating(0.0)
                .isVerified(false)
                .build();
    }
}
