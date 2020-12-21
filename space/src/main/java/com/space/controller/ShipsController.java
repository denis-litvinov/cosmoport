package com.space.controller;

import com.space.mistakes.BadRequest;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/ships")
public class ShipsController {

    @Autowired
    private final ShipServiceImpl shipService;

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
                              @RequestParam(value = "pageSize", required = false) Integer pageSize,
                              Model model) {


        List<Ship> list = shipService.findShipsByParams(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed,
                minCrewSize, maxCrewSize, minRating, maxRating);
        List<Ship> listAfterPaging = shipService.findShipsByPaging(list, shipOrder, pageNumber, pageSize);
        model.addAttribute("ships", listAfterPaging);
        return listAfterPaging;
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
                                 @RequestParam(value = "maxRating", required = false) Double maxRating,
                                 Model model) {
        Integer count = shipService.findShipsByParams(name, planet, shipType, after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating).size();
        model.addAttribute("count", count);
        return count;
    }

    @PostMapping
    @ResponseBody
    public Ship create(@RequestBody Ship ship) {
        return shipService.save(ship);
    }


}
