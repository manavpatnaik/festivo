package com.manav.festivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_providers",uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false)
    private ServiceType serviceType;

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name must be less than 100 characters")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "Address is required")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "City is required")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "State is required")
    @Column(name = "state", nullable = false)
    private String state;

    @NotBlank(message = "Country is required")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "Pincode is required")
    @Column(name = "pin_code", nullable = false)
    private String pinCode;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceProviderPricing> pricing = new ArrayList<>();

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceProviderAvailability> availability = new ArrayList<>();

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicePortfolio> portfolio = new ArrayList<>();

    public enum ServiceType {
        CATERER,
        MAKEUP_ARTIST,
        PHOTOGRAPHER,
        DECORATOR,
        VENUE_PROVIDER
    }
}
