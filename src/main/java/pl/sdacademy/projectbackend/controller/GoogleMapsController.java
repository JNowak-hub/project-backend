package pl.sdacademy.projectbackend.controller;

import com.google.maps.errors.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sdacademy.projectbackend.model.Location;
import pl.sdacademy.projectbackend.service.GoogleMapsApi;

import java.io.IOException;

@RestController
@RequestMapping("/api/google/maps")
public class GoogleMapsController {

    private GoogleMapsApi googleMapsApi;

    public GoogleMapsController(GoogleMapsApi googleMapsApi) {
        this.googleMapsApi = googleMapsApi;
    }


    @PostMapping("{locationName}")
    public ResponseEntity<Location> saveLocation(@PathVariable String locationName) throws InterruptedException, ApiException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(googleMapsApi.saveLocationFromGoogleMapsApi(locationName));
    }
}
