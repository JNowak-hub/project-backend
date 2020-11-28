package pl.sdacademy.projectbackend.configuration;

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
import pl.sdacademy.projectbackend.service.CustomAuthenticationService;
import pl.sdacademy.projectbackend.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
    private CustomAuthenticationService customAuthenticationService;
    private JwtRequestFilter jwtRequestFilter;

    public WebSecurityConfiguration(UserService userService, BCryptPasswordEncoder passwordEncoder, CustomAuthenticationService customAuthenticationService, JwtRequestFilter jwtRequestFilter) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationService = customAuthenticationService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .cors()
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/api/user/add").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .disable()
                .logout()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
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
}
