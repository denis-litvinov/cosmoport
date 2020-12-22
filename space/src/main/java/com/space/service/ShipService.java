package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;


import java.util.List;

public interface ShipService {
    List<Ship> findAll();
    List<Ship> findShipsByParams(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                 Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating);
    List<Ship> findShipsByPaging(List<Ship> shipList, ShipOrder order, Integer pageNumber, Integer pageSize);
    Ship save(Ship ship);
    Ship findById(Long id);
    Ship update(Ship ship, Long id);
    void delete(Long id);

}
