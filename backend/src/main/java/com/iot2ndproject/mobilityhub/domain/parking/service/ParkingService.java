package com.iot2ndproject.mobilityhub.domain.parking.service;

import com.iot2ndproject.mobilityhub.domain.parking.dto.ParkingDTO;
import java.util.List;

public interface ParkingService {
    List<ParkingDTO> getAllParkingSpots();
    ParkingDTO getParkingById(String sectorId);
    void createParking(ParkingDTO parkingDTO);
    void updateParking(ParkingDTO parkingDTO);
    void deleteParking(String sectorId);
}
