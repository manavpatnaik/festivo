package com.manav.festivo.controller;

import com.manav.festivo.config.AppConfig;
import com.manav.festivo.dto.ServicePortfolioCreationDTO;
import com.manav.festivo.model.ServicePortfolio;
import com.manav.festivo.service.ServicePortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/service-providers/{serviceProviderId}/portfolio")
@RequiredArgsConstructor
public class ServicePortfolioController {
    private final ServicePortfolioService portfolioService;
    private final AppConfig appConfig;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServicePortfolio> createPortfolio(
            @PathVariable UUID serviceProviderId,
            @RequestPart("eventType") String eventType,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

        System.out.println("Got request to create portfolio");
        ServicePortfolio.EventType eventTypeEnum;
        try {
            eventTypeEnum = ServicePortfolio.EventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid event type: " + eventType);
        }

        // Manually construct the DTO
        ServicePortfolioCreationDTO creationDTO = new ServicePortfolioCreationDTO();
        creationDTO.setEventType(eventType);
        creationDTO.setDescription(description);

        ServicePortfolio portfolio = creationDTO.toServicePortfolio();

        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(appConfig.getUploadDir(), fileName);
                Files.write(filePath, file.getBytes());
                imageUrls.add(filePath.toString());
            }
        }
        System.out.println("Setting image urls");
        portfolio.setImageUrls(imageUrls);
        ServicePortfolio createdPortfolio = portfolioService.createPortfolio(serviceProviderId, portfolio);
        System.out.println("Portfolio created: " + createdPortfolio);
        return new ResponseEntity<>(createdPortfolio, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePortfolio> getPortfolioById(@PathVariable UUID id) {
        ServicePortfolio portfolio = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping
    public ResponseEntity<List<ServicePortfolio>> getPortfolioByServiceProvider(
            @PathVariable UUID serviceProviderId,
            @RequestParam(required = false) ServicePortfolio.EventType eventType) {
        if (eventType != null) {
            try {
                ServicePortfolio.EventType eventTypeEnum = ServicePortfolio.EventType.valueOf(eventType.name());
                return ResponseEntity.ok(portfolioService.getPortfolioByServiceProviderAndEventType(serviceProviderId, eventTypeEnum));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid event type: " + eventType.name());
            }
        }
        List<ServicePortfolio> portfolioList = portfolioService.getPortfolioByServiceProvider(serviceProviderId);
        return ResponseEntity.ok(portfolioList);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServicePortfolio> updatePortfolio(
            @PathVariable UUID id,
            @RequestPart("eventType") String eventType,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {


        // Validate eventType
        ServicePortfolio.EventType eventTypeEnum;
        try {
            eventTypeEnum = ServicePortfolio.EventType.valueOf(eventType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid event type: " + eventType);
        }

        // Fetch the existing portfolio to get the old image URLs
        ServicePortfolio existingPortfolio = portfolioService.getPortfolioById(id);
        List<String> oldImageUrls = existingPortfolio.getImageUrls();

        // Manually construct the DTO
        ServicePortfolioCreationDTO updateDTO = new ServicePortfolioCreationDTO();
        updateDTO.setEventType(eventType);
        updateDTO.setDescription(description);

        ServicePortfolio portfolio = updateDTO.toServicePortfolio();

        // Handle new image uploads
        List<String> imagePaths = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            // Delete old images only if new images are provided
            if (oldImageUrls != null && !oldImageUrls.isEmpty()) {
                for (String oldImageUrl : oldImageUrls) {
                    try {
                        Path oldImagePath = Paths.get(oldImageUrl);
                        Files.deleteIfExists(oldImagePath);
                    } catch (IOException e) {
                        throw new RuntimeException("Image not found: " + oldImageUrl);
                    }
                }
            }

            // Upload new images
            for (MultipartFile image : images) {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(appConfig.getUploadDir(), fileName);
                Files.write(filePath, image.getBytes());
                imagePaths.add(filePath.toString());
            }
        } else {
            // Keep the old image URLs if no new images are provided
            imagePaths = oldImageUrls != null ? oldImageUrls : new ArrayList<>();
        }
        portfolio.setImageUrls(imagePaths);
        ServicePortfolio updatedPortfolio = portfolioService.updatePortfolio(id, portfolio);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable UUID id) {
        ServicePortfolio portfolio = portfolioService.getPortfolioById(id);
        List<String> imageUrls = portfolio.getImageUrls();

        // Delete images from the server
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                try {
                    Path imagePath = Paths.get(imageUrl);
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Error deleting image url: " + imageUrl);
                }
            }
        }
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }
}
