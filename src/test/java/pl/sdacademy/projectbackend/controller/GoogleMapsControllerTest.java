package pl.sdacademy.projectbackend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.sdacademy.projectbackend.configuration.CustomExceptionHandler;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Location;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.oauth.facebook.model.AuthProvider;
import pl.sdacademy.projectbackend.service.GoogleMapsApi;
import pl.sdacademy.projectbackend.service.UserService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
public class GoogleMapsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GoogleMapsApi googleMapsApi;
    @InjectMocks
    private GoogleMapsController controller;

    private Location location;

    @BeforeEach
    void mockSetup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        location = new Location();
        location.setAddress("address");
        location.setLat(10.10);
        location.setLng(20.20);
        location.setId(1L);
    }

    @Test
    @DisplayName("When call findUserById should return status 200 and User response body")
    public void test1() throws Exception {
        //given
        when(googleMapsApi.saveLocationFromGoogleMapsApi("address")).thenReturn(location);
        //when then
        mockMvc.perform(post("/api/google/maps/address"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.lat", is(location.getLat())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", is(location.getAddress())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lng", is(location.getLng())));
    }
}
