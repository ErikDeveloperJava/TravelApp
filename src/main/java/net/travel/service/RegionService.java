package net.travel.service;

import net.travel.model.Region;

import java.util.List;

public interface RegionService {

    List<Region> getAll();

    boolean existsById(int regionId);
}