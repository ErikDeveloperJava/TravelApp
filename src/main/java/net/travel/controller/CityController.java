package net.travel.controller;

import net.travel.model.City;
import net.travel.service.CityService;
import net.travel.service.RegionService;
import net.travel.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private NumberUtil numberUtil;

    @GetMapping("/by/regionId/{regionId}")
    public @ResponseBody
    ResponseEntity cities(@PathVariable("regionId")String regionStrId){
        int regionId = numberUtil.parseStrToInteger(regionStrId);
        if(regionId == -1 || !regionService.existsById(regionId)){
            return ResponseEntity
                    .notFound()
                    .build();
        }
        List<City> result = cityService.getAllByRegionId(regionId);
        return ResponseEntity
                .ok(result);
    }
}