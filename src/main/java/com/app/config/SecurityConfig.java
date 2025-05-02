package com.app.config;

import com.app.Filters.JWTFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration           //by this, default spring security features(urls) will not work & instead only permitted features will work
public class SecurityConfig {

    private JWTFilter jwtFilter;

    public SecurityConfig(JWTFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean                 //when the configuration file runs object of this method will be created & objects details passed to spring framework
    public SecurityFilterChain securityFilterChain(
         HttpSecurity http //automatically http object will be created
    ) throws Exception {
        //h(cd)2
        http.csrf().disable().cors().disable(); //Cross Site Request Forgery by enabling to get protection fromm cbyer attacks
        // by enabling cors the port nor will integrate in perticular client like react or angular or postman perticulary any one

        //haap - any request coming to http server will get permission without restrictions
       http.authorizeHttpRequests().anyRequest().permitAll();
//        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);  //run autherizationFilter before JWTFilter
//        http.authorizeHttpRequests()
//                .requestMatchers("/api/v1/auth/signup","/api/v1/auth/userSignin","/api/v1/auth/content-manager-signup",
//                "/api/v1/auth/blog-manager-signup","/api/v1/auth/login-otp")
//                .permitAll()
//                .requestMatchers("/api/v1/cars/add-car")//this url can be accessed by only content manager
//                .hasRole("CONTENT_MANAGER")//hasRole can have only 1 role & hasAnyRole can have multiple role
//                .requestMatchers("/api/v1/search-car/car","/api/s3/upload/car/{carId}","/api/s3/delete",
//                        "/api/v1/search-car/cars")
//                .permitAll()
//                .requestMatchers("/api/v1/crm","/api/brands/details/upload","/api/v1/auth/location").permitAll()
//                .anyRequest()
//                .authenticated();

        return http.build();
    }

//    @Bean             for 1st method of password encryption
//    public PasswordEncoder getPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
}
