package pl.sdacademy.projectbackend.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.model.Location;

import java.io.IOException;

@Service
public class GoogleMapsApi {

    private LocationService locationService;

    private String googleMapsApiKey;

    public GoogleMapsApi(LocationService locationService,
                         @Value("${google.maps.api.key}") String googleMapsApiKey) {
        this.locationService = locationService;
        this.googleMapsApiKey = googleMapsApiKey;
    }

    public Location saveLocationFromGoogleMapsApi(String locationName) throws InterruptedException, ApiException, IOException {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleMapsApiKey)
                .build();

        GeocodingResult[] results =  GeocodingApi.geocode(context,
                locationName).await();

        Location location = new Location();
        location.setAddress(results[0].formattedAddress);
        location.setLng(results[0].geometry.location.lng);
        location.setLat(results[0].geometry.location.lat);
        return locationService.saveLocation(location);
    }

}
