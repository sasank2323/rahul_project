package com.app.Filters;
//this class will get the token details you send the Token in postman

import com.app.entity.User;
import com.app.repository.UserRepository;
import com.app.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;


//OncePerRequestFilter in abstract class which has doFilter method which consists of request, response & filterchain
@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private UserRepository userRepository;
    public JWTFilter(JWTService jwtService, UserRepository userRepository){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(         //method overriding concept which comes from OncePerRequestFilter
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");//request has inbuilt method getHeader which has JWToken
        if(token != null && token.startsWith("Bearer ")){ //Bearer_ space is used bcs token will start with
            String jwtoken = token.substring(8,token.length()-1); //started from 8 because to remove bearer & space also "
            String username = jwtService.getUsername(jwtoken);
            Optional<User> opUser = userRepository.findByUsername(username);
            if(opUser.isPresent()) {
                User user = opUser.get();   //when JWToken is valid for this user we have to get details from database.
                //this details next be taken to spring Security & set the username, password for the above user & after grant me permission to access url
                //JWT details are not dstored in spring security then token is invalid

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,  //Single-1 ton-object collection that have one object
                        Collections.singleton(new SimpleGrantedAuthority(user.getRole())));//it is kept null when authorities security role concept is in use
                authenticationToken.setDetails(new WebAuthenticationDetails(request));  //ASNWR -Australia Sets New World Record

                //now to store in Security context folder
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                
            }
        }
        filterChain.doFilter(request,response);   //when token is false this filterchain has interal logic which will redirect where it is to be
    }


}
