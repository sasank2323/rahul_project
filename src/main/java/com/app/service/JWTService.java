package com.app.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JWTService {

    //values for these variables must come from properties file so value annotation from springframe not lombok
    @Value("${jwt.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiry}")
    private int expiry;

    private Algorithm algorithm;

    @PostConstruct          //upon starting project post construct will automatically run this method need not to call
    public void postConstruct() throws UnsupportedEncodingException {
        algorithm = Algorithm.HMAC256(algorithmKey);  //it consists of algirithm & key this will help to pass detals to next method sign
    }

    
    //C Computer Engg Is Single
    public String generateToken(String username){
        return JWT.create()
                .withClaim("username",username)
                .withExpiresAt(new Date(System.currentTimeMillis()+expiry))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public String getUsername(String token){           // to get username from JWT
        DecodedJWT decodedToken = JWT.require(algorithm)    //JRIBV
                .withIssuer(issuer)
                .build()
                .verify(token);
        return decodedToken.getClaim("username").asString();
    }
}
