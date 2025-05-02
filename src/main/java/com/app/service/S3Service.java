package com.app.service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    // Constructor injection
    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Convert MultipartFile to Path
        Path tempFile = Files.createTempFile(fileName, null);
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Upload file to S3
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(tempFile));
            Files.delete(tempFile);  // Clean up the temporary file

        } catch (S3Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file to S3", e);
        }

        // Construct the file's URL
        String fileUrl = String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
//this will return "https://myawsbucket821.s3.amazonaws.com/1739349857856_birdIMG.jpg"
        return fileUrl;  // Return the URL of the uploaded file
    }

    public byte[] downloadFile(String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.getObject(getObjectRequest).readAllBytes();
    }

    public String deleteFile(String fileName) {
        try {
            // Create the delete request
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Perform the delete operation
            s3Client.deleteObject(deleteObjectRequest);

            return "File deleted successfully: " + fileName;
        } catch (S3Exception e) {
            e.printStackTrace();
            return "Failed to delete file: " + e.getMessage();
        }
    }
}
