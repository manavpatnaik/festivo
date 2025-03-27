package com.manav.festivo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "service_provider_pricing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProviderPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProvider serviceProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @ElementCollection
    @CollectionTable(name = "pricing_tiers", joinColumns = @JoinColumn(name = "service_provider_pricing_id"))
    private List<PricingTier> pricingTiers = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum EventType {
        WEDDING,
        BIRTHDAY,
        CORPORATE,
        BABY_SHOWER,
        ANNIVERSARY
    }
}
