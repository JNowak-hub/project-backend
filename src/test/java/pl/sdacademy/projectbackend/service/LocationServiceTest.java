package pl.sdacademy.projectbackend.service;

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
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private Location location;

    @BeforeEach
    void setup(){
        location = new Location();
        location.setAddress("adress");
        location.setId(1L);
        location.setLat(10.10);
        location.setLng(12.12);
    }

    @Test
    @DisplayName("When saveLocation with not null Location than returned saved location")
    public void test1(){
        //given
        when(locationRepository.save(location)).thenReturn(location);
        //when
        Location savedLocation = locationService.saveLocation(location);
        //then
        assertThat(location).isEqualTo(savedLocation);
    }

    @Test
    @DisplayName("When saveLocation with null location then trow BadRequestException")
    public void test2(){
        //given
        location = null;
        //when
        BadRequestException exception = assertThrows(BadRequestException.class, () -> locationService.saveLocation(location));
        //then
        verify(locationRepository, never()).save(location);
    }
}
