package com.iot2ndproject.mobilityhub.domain.work.service;

import com.iot2ndproject.mobilityhub.domain.vehicle.dao.CarDAO;
import com.iot2ndproject.mobilityhub.domain.work.dao.ServiceRequestDAO;
import com.iot2ndproject.mobilityhub.domain.work.dto.ServiceRequestDTO;
import com.iot2ndproject.mobilityhub.domain.work.entity.ServiceRequestEntity;
import com.iot2ndproject.mobilityhub.domain.work.entity.WorkInfoEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestDAO serviceRequestDAO;
private final CarDAO carDAO;
    @Override
    @Transactional
    public ServiceRequestDTO create(ServiceRequestDTO dto) {
        

        WorkInfoEntity entity = new WorkInfoEntity();
        entity.setCar(carDAO.findByCarNumber(dto.getCarNumber()));
        entity.set
        
        .builder()
                .userId(dto.getUserId())
                .carNumber(dto.getCarNumber())
                .services(String.join(",", dto.getServices()))
                .additionalRequest(dto.getAdditionalRequest())
                .status("REQUESTED")
                .parkingStatus(dto.getServices().contains("parking") ? "REQUESTED" : null)
                .carwashStatus(dto.getServices().contains("carwash") ? "REQUESTED" : null)
                .maintenanceStatus(dto.getServices().contains("maintenance") ? "REQUESTED" : null)
                .build();
        
        ServiceRequestEntity saved = serviceRequestDAO.save(entity);
        return convertToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceRequestDTO> getHistory(String userId) {
        return serviceRequestDAO.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceRequestDTO> getLatest(String userId) {
        return serviceRequestDAO.findTop1ByUserIdOrderByCreatedAtDesc(userId)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public boolean updateStatus(Long id, String status, String service) {
        Optional<ServiceRequestEntity> optionalEntity = serviceRequestDAO.findById(id);
        if (optionalEntity.isEmpty()) {
            return false;
        }

        ServiceRequestEntity entity = optionalEntity.get();
        
        if (service == null) {
            entity.setStatus(status);
        } else {
            updateServiceStatus(entity, service, status);
        }
        
        updateOverallStatus(entity);
        serviceRequestDAO.save(entity);
        return true;
    }

    private void updateServiceStatus(ServiceRequestEntity entity, String service, String status) {
        switch (service) {
            case "parking" -> entity.setParkingStatus(status);
            case "carwash" -> entity.setCarwashStatus(status);
            case "maintenance" -> entity.setMaintenanceStatus(status);
        }
    }

    private void updateOverallStatus(ServiceRequestEntity entity) {
        List<String> statuses = List.of(
            entity.getParkingStatus(),
            entity.getCarwashStatus(),
            entity.getMaintenanceStatus()
        );

        List<String> activeStatuses = statuses.stream()
                .filter(s -> s != null)
                .toList();

        if (activeStatuses.isEmpty()) {
            return;
        }

        boolean allDone = activeStatuses.stream()
                .allMatch(s -> s.equalsIgnoreCase("DONE"));
        boolean anyInProgress = activeStatuses.stream()
                .anyMatch(s -> s.equalsIgnoreCase("IN_PROGRESS"));

        if (allDone) {
            entity.setStatus("DONE");
        } else if (anyInProgress) {
            entity.setStatus("IN_PROGRESS");
        }
    }

    private ServiceRequestDTO convertToDTO(ServiceRequestEntity entity) {
        ServiceRequestDTO dto = new ServiceRequestDTO();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setCarNumber(entity.getCarNumber());
        dto.setServices(List.of(entity.getServices().split(",")));
        dto.setAdditionalRequest(entity.getAdditionalRequest());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        dto.setParkingStatus(entity.getParkingStatus());
        dto.setCarwashStatus(entity.getCarwashStatus());
        dto.setMaintenanceStatus(entity.getMaintenanceStatus());
        return dto;
    }
}
