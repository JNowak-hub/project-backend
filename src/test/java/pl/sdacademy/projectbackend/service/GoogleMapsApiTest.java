package pl.sdacademy.projectbackend.service;

import com.google.maps.GeocodingApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sdacademy.projectbackend.exceptions.BadRequestException;
import pl.sdacademy.projectbackend.model.Location;
import pl.sdacademy.projectbackend.repository.LocationRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GoogleMapsApiTest {

    @Mock
    private LocationService locationService;
    @Mock
    private GeocodingApi geocodingApi;

    private GoogleMapsApi googleMapsApi;

    private Location location;

    @BeforeEach
    void setup() {
        googleMapsApi = new GoogleMapsApi(locationService, "secret");

        location = new Location();
        location.setAddress("adress");
        location.setId(1L);
        location.setLat(10.10);
        location.setLng(12.12);
    }

}
