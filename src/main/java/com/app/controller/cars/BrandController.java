package com.app.controller.cars;

import com.app.service.carservices.BrandNameUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/brands/details")
public class BrandController {

    @Autowired
    private BrandNameUploadService brandService;

    // Endpoint to upload file
    //http://localhost:8080/api/brands/details/upload
    @PostMapping("/upload")
    public ResponseEntity<String> uploadBrandFile(@RequestParam("file") MultipartFile file) {
        try {
            // Pass the uploaded MultipartFile to the service
            brandService.uploadBrands(file);
            return new ResponseEntity<>("File uploaded and brands added successfully!", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
