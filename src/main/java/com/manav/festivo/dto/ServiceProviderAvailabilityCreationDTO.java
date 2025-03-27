package com.manav.festivo.dto;

import com.manav.festivo.model.ServiceProviderAvailability;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ServiceProviderAvailabilityCreationDTO {
    @NotNull(message = "Availability date is required")
    private LocalDate availabilityDate;

    @NotBlank(message = "Status is required")
    private String status;

    public ServiceProviderAvailability toServiceProviderAvailability() {
        ServiceProviderAvailability availability = new ServiceProviderAvailability();
        availability.setAvailabilityDate(availabilityDate);
        availability.setStatus(ServiceProviderAvailability.AvailabilityStatus.valueOf(status.toUpperCase()));
        return availability;
    }
}
