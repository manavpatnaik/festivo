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
@Table(name = "service_portfolio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicePortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProvider serviceProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "description", length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "portfolio_images", joinColumns = @JoinColumn(name = "service_portfolio_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

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
