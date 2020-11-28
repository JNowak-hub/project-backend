package pl.sdacademy.projectbackend.oauth.facebook.security;




import pl.sdacademy.projectbackend.exceptions.OAuth2AuthenticationProcessingException;
import pl.sdacademy.projectbackend.oauth.facebook.model.AuthProvider;
import pl.sdacademy.projectbackend.oauth.facebook.model.FacebookOAuth2UserInfo;
import pl.sdacademy.projectbackend.oauth.facebook.model.OAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
