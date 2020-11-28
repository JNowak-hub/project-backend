package pl.sdacademy.projectbackend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.sdacademy.projectbackend.filter.JwtRequestFilter;
import pl.sdacademy.projectbackend.oauth.facebook.security.*;
import pl.sdacademy.projectbackend.oauth.facebook.service.CustomOAuth2UserService;
import pl.sdacademy.projectbackend.service.CustomAuthenticationService;
import pl.sdacademy.projectbackend.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
    private CustomAuthenticationService customAuthenticationService;
    private JwtRequestFilter jwtRequestFilter;
    private CustomOAuth2UserService customOAuth2UserService;
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;


    public WebSecurityConfiguration(UserService userService, BCryptPasswordEncoder passwordEncoder, CustomAuthenticationService customAuthenticationService, JwtRequestFilter jwtRequestFilter, CustomOAuth2UserService customOAuth2UserService, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler, OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationService = customAuthenticationService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
        this.oAuth2AuthenticationFailureHandler = oAuth2AuthenticationFailureHandler;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .cors()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/login",
                        "/api/user/add",
                        "/auth/**",
                        "/oauth2/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .disable()
                .logout()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Primary
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return customAuthenticationService;
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public JwtRequestFilter tokenAuthenticationFilter() {
        return jwtRequestFilter;
    }
}
