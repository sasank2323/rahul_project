package com.app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoLocationService {

    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your actual API Key
    private static final String URL = "http://api.ipstack.com/";  //IpStack Access Key : f4ef4545fa34695c9b0ee75bbcda4bec

    // Method to get geolocation data for a given IP address
    public String getLocation(String ipAddress) {
        RestTemplate restTemplate = new RestTemplate();
        String url = URL + ipAddress + "?access_key=" + API_KEY;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
