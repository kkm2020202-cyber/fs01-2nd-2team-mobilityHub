package com.iot2ndproject.mobilityhub.domain.work.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String carNumber;

    @Column(nullable = false)
    private String services; // comma-separated

    @Column(length = 1000)
    private String additionalRequest;

    @Column(nullable = false)
    private String status; // REQUESTED, IN_PROGRESS, DONE

    @Column
    private String parkingStatus; // nullable if not requested

    @Column
    private String carwashStatus; // nullable if not requested

    @Column
    private String maintenanceStatus; // nullable if not requested

    @CreationTimestamp
    private LocalDateTime createdAt;
}

