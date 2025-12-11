package com.iot2ndproject.mobilityhub.domain.work.controller;

import com.iot2ndproject.mobilityhub.domain.work.dto.ServiceRequestDTO;
import com.iot2ndproject.mobilityhub.domain.work.entity.ServiceRequestEntity;
import com.iot2ndproject.mobilityhub.domain.work.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/service-request")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestRepository serviceRequestRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ServiceRequestDTO dto){
        ServiceRequestEntity entity = ServiceRequestEntity.builder()
                .userId(dto.getUserId())
                .carNumber(dto.getCarNumber())
                .services(String.join(",", dto.getServices()))
                .additionalRequest(dto.getAdditionalRequest())
                .status("REQUESTED")
                .parkingStatus(dto.getServices().contains("parking") ? "REQUESTED" : null)
                .carwashStatus(dto.getServices().contains("carwash") ? "REQUESTED" : null)
                .maintenanceStatus(dto.getServices().contains("maintenance") ? "REQUESTED" : null)
                .build();
        return ResponseEntity.ok(serviceRequestRepository.save(entity));
    }

    @GetMapping
    public ResponseEntity<List<ServiceRequestDTO>> history(@RequestParam("userId") String userId){
        List<ServiceRequestDTO> responses = serviceRequestRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(entity -> {
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
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/latest")
    public ResponseEntity<ServiceRequestDTO> latest(@RequestParam("userId") String userId){
        return serviceRequestRepository.findTop1ByUserIdOrderByCreatedAtDesc(userId)
                .map(entity -> {
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
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestParam("status") String status,
                                          @RequestParam(value = "service", required = false) String service){
        ServiceRequestEntity entity = serviceRequestRepository.findById(id).orElse(null);
        if(entity == null){
            return ResponseEntity.notFound().build();
        }
        if(service == null){
            entity.setStatus(status);
        }else{
            switch (service){
                case "parking" -> entity.setParkingStatus(status);
                case "carwash" -> entity.setCarwashStatus(status);
                case "maintenance" -> entity.setMaintenanceStatus(status);
            }
        }
        // 전체 상태는 개별 상태가 모두 DONE이면 DONE, 하나라도 IN_PROGRESS면 IN_PROGRESS, 나머지는 전달된 status 우선
        if(entity.getParkingStatus()!=null || entity.getCarwashStatus()!=null || entity.getMaintenanceStatus()!=null){
            boolean allDone = Stream.of(entity.getParkingStatus(), entity.getCarwashStatus(), entity.getMaintenanceStatus())
                    .filter(s -> s != null)
                    .allMatch(s -> s.equalsIgnoreCase("DONE"));
            boolean anyInProgress = Stream.of(entity.getParkingStatus(), entity.getCarwashStatus(), entity.getMaintenanceStatus())
                    .filter(s -> s != null)
                    .anyMatch(s -> s.equalsIgnoreCase("IN_PROGRESS"));
            if(allDone){
                entity.setStatus("DONE");
            }else if(anyInProgress){
                entity.setStatus("IN_PROGRESS");
            }else{
                entity.setStatus(status);
            }
        }
        serviceRequestRepository.save(entity);
        return ResponseEntity.ok().build();
    }
}

