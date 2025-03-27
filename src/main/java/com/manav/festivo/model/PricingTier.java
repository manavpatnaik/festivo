package com.manav.festivo.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingTier {
    @NotBlank(message = "Tier name is required")
    @Size(max = 50, message = "Tier name must be less than 50 characters")
    private String tierName;

    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    @Size(max = 200, message = "Description must be less than 200 characters")
    private String description;

    @Min(value = 1, message = "Minimum guest range must be at least 1")
    private int guestRangeMin;

    @Min(value = 1, message = "Maximum guest range must be at least 1")
    private int guestRangeMax;
}
