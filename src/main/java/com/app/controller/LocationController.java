package com.app.controller;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LocationController {

    // Replace with your actual API key from ipinfo.io
    private final String ipInfoApiKey = "19ac7dd9c63cbe";

    @GetMapping("/location")
    public String getUsersLocation() {
        RestTemplate restTemplate = new RestTemplate();

        // Step 1: Get the public IP address using ipify API
        String ipifyResponse = restTemplate.getForObject("http://api.ipify.org?format=json", String.class);

        // Parse the JSON response to extract the IP address
        String ipAddress = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseNode = objectMapper.readTree(ipifyResponse);
            ipAddress = responseNode.get("ip").asText(); // Extract the IP address from the response
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while parsing the IP response";
        }

        // Step 2: Get location info using IP address via ipinfo.io
        String locationApiUrl = "https://ipinfo.io/" + ipAddress + "/json?token=" + ipInfoApiKey;
        String locationResponse = restTemplate.getForObject(locationApiUrl, String.class);

        // Step 3: Parse the ipinfo.io response to get location details
        String locationDetails = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode locationResponseNode = objectMapper.readTree(locationResponse);

            // Extract additional details
            String city = locationResponseNode.get("city").asText();
            String region = locationResponseNode.get("region").asText();
            String country = locationResponseNode.get("country").asText();
            String postal = locationResponseNode.has("postal") ? locationResponseNode.get("postal").asText() : "N/A";
            String location = locationResponseNode.get("loc").asText(); // Latitude, Longitude
            String hostname = locationResponseNode.has("hostname") ? locationResponseNode.get("hostname").asText() : "N/A";
            String org = locationResponseNode.has("org") ? locationResponseNode.get("org").asText() : "N/A";

            // Format the location details with all the info
            locationDetails = String.format(
                    "City: %s, Region: %s, Country: %s, Postal Code: %s, Location (Lat, Long): %s, Hostname: %s, Organization: %s",
                    city, region, country, postal, location, hostname, org
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while parsing the location response";
        }

        // Step 4: Return the location details (city, region, country, postal, location, etc.)
        return locationDetails;
    }
}
