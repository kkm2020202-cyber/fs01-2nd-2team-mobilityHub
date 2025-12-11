package com.iot2ndproject.mobilityhub.domain.work.repository;

import com.iot2ndproject.mobilityhub.domain.work.entity.ServiceRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequestEntity, Long> {
    List<ServiceRequestEntity> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<ServiceRequestEntity> findTop1ByUserIdOrderByCreatedAtDesc(String userId);
}

