package net.travel.controller;

import net.travel.model.Region;
import net.travel.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public @ResponseBody
    ResponseEntity regions(){
        List<Region> result = regionService.getAll();
        return ResponseEntity
                .ok(result);
    }
}