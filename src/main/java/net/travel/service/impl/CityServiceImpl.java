package net.travel.service.impl;

import net.travel.model.City;
import net.travel.repository.CityRepository;
import net.travel.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<City> getAllByRegionId(int regionId) {
        return cityRepository.findAllByRegionId(regionId);
    }
}
