package net.travel.service;

import net.travel.model.City;

import java.util.List;

public interface CityService {

    List<City> getAllByRegionId(int regionId);
}