package pl.sdacademy.projectbackend.service;

import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.BadRequestException;
import pl.sdacademy.projectbackend.model.Location;
import pl.sdacademy.projectbackend.repository.LocationRepository;

@Service
public class LocationService {

    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location saveLocation(Location location){
        if(location == null){
            throw new BadRequestException("Location can not be null");
        }
        return locationRepository.save(location);
    }
}
