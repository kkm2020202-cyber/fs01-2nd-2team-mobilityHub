package com.iot2ndproject.mobilityhub.domain.parking.service;

import com.iot2ndproject.mobilityhub.domain.parking.dao.ParkingDAO;
import com.iot2ndproject.mobilityhub.domain.parking.dto.ParkingDTO;
import com.iot2ndproject.mobilityhub.domain.parking.entity.ParkingEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService {

    private final ParkingDAO parkingDAO;
    private final ModelMapper modelMapper;

    // 모든 주차장 조회
    @Override
    public List<ParkingDTO> getAllParkingSpots() {
        List<ParkingEntity> parkings = parkingDAO.findAll();
        return parkings.stream()
                .map(parking -> modelMapper.map(parking, ParkingDTO.class))
                .collect(Collectors.toList());
    }

    // ID로 특정 주차장 조회
    @Override
    public ParkingDTO getParkingById(String sectorId) {
        ParkingEntity parking = parkingDAO.findById(sectorId);
        return modelMapper.map(parking, ParkingDTO.class);
    }

    // 주차장 생성
    @Override
    public void createParking(ParkingDTO parkingDTO) {
        ParkingEntity parking = modelMapper.map(parkingDTO, ParkingEntity.class);
        parkingDAO.save(parking);
    }

    // 주차장 수정
    @Override
    public void updateParking(ParkingDTO parkingDTO) {
        ParkingEntity parking = modelMapper.map(parkingDTO, ParkingEntity.class);
        parkingDAO.update(parking);
    }

    // 주차장 삭제
    @Override
    public void deleteParking(String sectorId) {
        parkingDAO.delete(sectorId);
    }
}
