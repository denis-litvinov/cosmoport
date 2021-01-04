package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/ships")
public class ShipsController {


    private ShipServiceImpl shipService;

    @Autowired
    public void setShipService(ShipServiceImpl shipService){
        this.shipService = shipService;
    }

    public ShipsController(ShipServiceImpl shipService) {
        this.shipService = shipService;
    }

    @GetMapping()
    @ResponseBody
    public List<Ship> findAll(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "planet", required = false) String planet,
                              @RequestParam(value = "shipType", required = false) ShipType shipType,
                              @RequestParam(value = "after", required = false) Long after,
                              @RequestParam(value = "before", required = false) Long before,
                              @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                              @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                              @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                              @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                              @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                              @RequestParam(value = "minRating", required = false) Double minRating,
                              @RequestParam(value = "maxRating", required = false) Double maxRating,
                              @RequestParam(value = "order", required = false) ShipOrder shipOrder,
                              @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {


        List<Ship> list = shipService.findShipsByParams(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating);
        return shipService.findShipsByPaging(list, shipOrder, pageNumber, pageSize);
    }

    @GetMapping("/count")
    @ResponseBody
    public Integer getShipsCount(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "planet", required = false) String planet,
                                 @RequestParam(value = "shipType", required = false) ShipType shipType,
                                 @RequestParam(value = "after", required = false) Long after,
                                 @RequestParam(value = "before", required = false) Long before,
                                 @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                 @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                 @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                 @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                 @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                 @RequestParam(value = "minRating", required = false) Double minRating,
                                 @RequestParam(value = "maxRating", required = false) Double maxRating) {
        return shipService.findShipsByParams(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating).size();
    }

    @PostMapping
    @ResponseBody
    public Ship create(@RequestBody Ship ship) {
        return shipService.save(ship);
    }

    @GetMapping("{id}")
    @ResponseBody
    public Ship findById(@PathVariable Long id){
        return shipService.findById(id);
    }

    @PostMapping("{id}")
    @ResponseBody
    public Ship update(@RequestBody Ship ship, @PathVariable Long id){
        return shipService.update(ship, id);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public void delete(@PathVariable Long id){
        shipService.delete(id);
    }


}
