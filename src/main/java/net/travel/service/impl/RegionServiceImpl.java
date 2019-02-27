package net.travel.service.impl;

import net.travel.model.Region;
import net.travel.repository.RegionRepository;
import net.travel.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Override
    public List<Region> getAll() {
        return regionRepository.findAll();
    }

    @Override
    public boolean existsById(int regionId) {
        return regionRepository.existsById(regionId);
    }
}
