package com.app.controller;

import com.app.entity.CarImage;
import com.app.entity.cars.Car;
import com.app.repository.CarImageRepository;
import com.app.repository.CarRepository;
import com.app.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;
    private CarRepository carRepository;
    private CarImageRepository carImageRepository;
    @Autowired
    public S3Controller(S3Service s3Service,CarRepository carRepository,CarImageRepository carImageRepository) {
        this.s3Service = s3Service;
        this.carRepository = carRepository;
        this.carImageRepository = carImageRepository;
    }

    // http://localhost:8080/api/s3/upload/car/{carId}
    @PostMapping("/upload/car/{carId}")
    public ResponseEntity<?> uploadFile(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable long carId
            ) {
//        try {
//            // Get the file URL after uploading
//            String url = s3Service.uploadFile(file);
//            Car car = carRepository.findById(carId).get();
//            CarImage image = new CarImage();
//            image.setUrl(url);
//            image.setCar(car);
//            CarImage savedImage = carImageRepository.save(image);
//            return new ResponseEntity<>(savedImage, HttpStatus.OK);  // Return the file URL
//        }
        try {
            // Loop through files and upload each
            if (files.length != 3) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("You must upload exactly 3 images, not less or more.");
            }
            for (MultipartFile file : files) {
                // Get the file URL after uploading
                String url = s3Service.uploadFile(file);

                Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
                CarImage image = new CarImage();
                image.setUrl(url);
                image.setCar(car);
                carImageRepository.save(image);  // Save each image URL to the database
            }
            return ResponseEntity.status(HttpStatus.OK).body("Files uploaded successfully!");
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    // http://localhost:8080/api/s3/delete
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestBody String file) {
        try {
            s3Service.deleteFile(file);
            return ResponseEntity.ok("File deleted successfully: " + file);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete file: " + e.getMessage());
        }
    }
}
