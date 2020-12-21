package com.space.service;

import com.space.controller.ShipOrder;
import com.space.mistakes.BadRequest;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Comparator;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    @Override
    public List<Ship> findShipsByParams(String name, String planet, ShipType shipType, Long after,
                                        Long before, Boolean isUsed, Double minSpeed, Double maxSpeed,
                                        Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {

        List<Ship> allShipsWithParams = findAll();

        if (name != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getName().contains(name)).collect(Collectors.toList());
        }
        if (planet != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getPlanet().contains(planet)).collect(Collectors.toList());
        }
        if (shipType != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getShipType().equals(shipType)).collect(Collectors.toList());
        }
        if (after != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getProdDate().after(new Date(after))).collect(Collectors.toList());
        }
        if (before != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getProdDate().before(new Date(before))).collect(Collectors.toList());
        }
        if (isUsed != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getUsed().equals(isUsed)).collect(Collectors.toList());
        }
        if (minSpeed != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getSpeed() >= minSpeed).collect(Collectors.toList());
        }
        if (maxSpeed != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getSpeed() <= maxSpeed).collect(Collectors.toList());
        }
        if (minCrewSize != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getCrewSize() >= minCrewSize).collect(Collectors.toList());
        }
        if (maxCrewSize != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getCrewSize() <= maxCrewSize).collect(Collectors.toList());
        }
        if (minRating != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getRating() >= minRating).collect(Collectors.toList());
        }
        if (maxRating != null) {
            allShipsWithParams = allShipsWithParams.stream().filter(ship -> ship.getRating() <= maxRating).collect(Collectors.toList());
        }

        return allShipsWithParams;
    }

    @Override
    public List<Ship> findShipsByPaging(List<Ship> shipList, ShipOrder order, Integer pageNumber, Integer pageSize) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageSize == null) {
            pageSize = 3;
        }
        return shipList.stream().sorted(getComparator(order)).skip(pageNumber * pageSize).limit(pageSize).collect(Collectors.toList());
    }


    @Override
    public Ship save(Ship ship) {
        if (ship.getName() == null ||
                ship.getPlanet() == null ||
                ship.getShipType() == null ||
                ship.getProdDate() == null ||
                ship.getSpeed() == null ||
                ship.getCrewSize() == null) {
            throw new BadRequest();
        }
        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }
        if ((ship.getName().isEmpty() || ship.getPlanet().isEmpty()) ||
                (ship.getName().length()>50 || ship.getPlanet().length() > 50)) {
            throw new BadRequest();
        }
        if ((ship.getSpeed() < 0.01d || ship.getSpeed() > 0.99d)
                || (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999)) {
            throw new BadRequest();
        }
        if (ship.getProdDate().getTime() < 0) {
            throw new BadRequest();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(ship.getProdDate());
        int year = cal.get(Calendar.YEAR);

        if (year < 2800 || year > 3019){
            throw new BadRequest();
        }

        ship.setRating(getRating(ship));
        return shipRepository.save(ship);
    }


    private Comparator<Ship> getComparator(ShipOrder order) {
        if (order == null) {
            return Comparator.comparing(Ship::getId);
        }

        Comparator<Ship> comparator = null;
        switch (order.getFieldName()) {
            case "id":
                comparator = Comparator.comparing(Ship::getId);
                break;
            case "speed":
                comparator = Comparator.comparing(Ship::getSpeed);
                break;
            case "prodDate":
                comparator = Comparator.comparing(Ship::getProdDate);
                break;
            case "rating":
                comparator = Comparator.comparing(Ship::getRating);
                break;
        }
        return comparator;
    }

    private Double getRating(Ship ship) {
        Double speed = ship.getSpeed();
        Double koef = ship.getUsed() ? 0.5 : 1;
        Double yearNow = 1.0 * 3019;
        Calendar year = Calendar.getInstance();
        year.setTime(ship.getProdDate());
        Double yearShip = 1.0 * year.get(Calendar.YEAR);
        Double result = (80 * speed * koef) / (yearNow - yearShip + 1);
        return (double) Math.round(result * 100)/100;
    }


}
