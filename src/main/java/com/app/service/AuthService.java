package com.app.service;

import com.app.entity.User;
import com.app.payload.LoginDto;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

//    private PasswordEncoder passwordEncoder;  1st method encrypting
//    public AuthService(PasswordEncoder passwordEncoder){
//        this.passwordEncoder = passwordEncoder;
//    }

    private UserRepository userRepository;
    private JWTService jwtService;
    public AuthService(UserRepository userRepository,JWTService jwtService){
        this.userRepository=userRepository;
        this.jwtService = jwtService;
    }

    public ResponseEntity<?> createContentManagerAccount(User user) {
        Optional<User>opUsername = userRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("username exists", HttpStatus.INTERNAL_SERVER_ERROR) ;
        }

        Optional<User>opEmail = userRepository.findByEmailId(user.getEmailId());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("email exists", HttpStatus.INTERNAL_SERVER_ERROR) ;
        }

        //1st method of encrypting password means it will be not shown directly in db
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);

        //2nd method of encrypting password means it will be not shown directly in db this has decent amount of encryption
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashpw);
        user.setRole("ROLE_CONTENT_MANAGER");  //its compulsory to write this ROLE_MANAGER in spring security whenever we want to give role
        userRepository.save(user);
        return new ResponseEntity<>("ctreated",HttpStatus.CREATED);
    }

    public ResponseEntity<?> createBlogManagerAccount(User user) {
        Optional<User>opUsername = userRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("username exists", HttpStatus.INTERNAL_SERVER_ERROR) ;
        }
    Optional<User>opEmail = userRepository.findByEmailId(user.getEmailId());
        if(opEmail.isPresent()){
        return new ResponseEntity<>("email exists", HttpStatus.INTERNAL_SERVER_ERROR) ;
    }
    String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashpw);
        user.setRole("ROLE_BLOG_MANAGER");
        userRepository.save(user);
        return new ResponseEntity<>("ctreated",HttpStatus.CREATED);
    }

    public ResponseEntity<?> createUser(User user) {
        Optional<User>opUsername = userRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("username exists", HttpStatus.INTERNAL_SERVER_ERROR) ;
        }
        Optional<User>opEmail = userRepository.findByEmailId(user.getEmailId());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("email exists", HttpStatus.INTERNAL_SERVER_ERROR) ;
        }
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
        user.setPassword(hashpw);
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return new ResponseEntity<>("ctreated",HttpStatus.CREATED);
    }

    public String verifyLogin(
            LoginDto dto
    ){
        Optional<User> opUser = userRepository.findByUsername(dto.getUsername());

        if(opUser.isPresent()){
            User user = opUser.get();
            if( BCrypt.checkpw(dto.getPassword(),user.getPassword())){
               return jwtService.generateToken(user.getUsername());
            }  // this will compare dto pass & user pass if valid returns token
        }

        return null;
    }
}
