package com.app.controller;

import com.app.entity.User;
import com.app.payload.JWTTokenDTO;
import com.app.payload.LoginDto;
import com.app.repository.UserRepository;
import com.app.service.AuthService;
import com.app.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private AuthService authService;
    private OTPService otpService;

     public AuthController(AuthService authService, OTPService otpService){
        this.authService = authService;
        this.otpService = otpService;
    }

    //http://localhost:8080/api/v1/auth/signup
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(
            @RequestBody User user
    ){

        ResponseEntity<?> response = authService.createUser(user);
        return response;
    }

    //http://localhost:8080/api/v1/auth/content-manager-signup
    @PostMapping("/content-manager-signup")
    public ResponseEntity<?> createContentManagerAccount(
            @RequestBody User user
    ){

        ResponseEntity<?> response = authService.createContentManagerAccount(user);
        return response;
    }

    //http://localhost:8080/api/v1/auth/blog-manager-signup
    @PostMapping("/blog-manager-signup")
    public ResponseEntity<?> createBlogManagerAccount(
            @RequestBody User user
    ){

        ResponseEntity<?> response = authService.createBlogManagerAccount(user);
        return response;
    }

    //Authentication means only after logging in using acc details & password one can access urls
    //http://localhost:8080/api/v1/auth/userSignin
    @PostMapping("/userSignin")
    public ResponseEntity<?> userSignIn(
            @RequestBody LoginDto dto
    ){
        String jwtToken = authService.verifyLogin(dto);
        if(jwtToken!=null){
            JWTTokenDTO tokenDTO = new JWTTokenDTO();
            tokenDTO.setToken(jwtToken);
            tokenDTO.setTokenType("JWT");
            return new ResponseEntity<>(tokenDTO, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Invalid Token", HttpStatus.CREATED);
    }

    //http://localhost:8080/api/v1/auth/login-otp?mobile
    @PostMapping("/login-otp")
    public String generateOtp(
            @RequestParam String mobile){
        String otp = otpService.generateOTP(mobile);
        return otp + "    " + mobile;
    }

    //http://localhost:8080/api/v1/auth/location
    @GetMapping("/location")
    public String getUsersLocation(){
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject("http://api.ipify.org?format=json", String.class);
        return forObject;
    } //make this method such that ot will just get public IPAddress & by that get longitude & lattitude later go on with the json location
}
