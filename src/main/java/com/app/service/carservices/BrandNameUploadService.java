package com.app.service.carservices;

import com.app.service.ExcelFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class BrandNameUploadService {

    @Autowired
    private ExcelFileReader excelFileReader;

    // This method will now handle the file directly passed from the controller
    public void uploadBrands(MultipartFile file) throws IOException {
        // Get the InputStream from the MultipartFile
        InputStream inputStream = file.getInputStream();

        // Bulk upload brands
        excelFileReader.bulkUploadBrandsFromInputStream(inputStream);
    }
}
