package com.app.service;

import com.app.entity.cars.Brand;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
public class ExcelFileReader {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void bulkUploadBrandsFromInputStream(InputStream inputStream) throws IOException {
        // Open the Excel file from InputStream
        Workbook workbook = new XSSFWorkbook(inputStream);

        // Get the first sheet
        Sheet sheet = workbook.getSheetAt(0);

        // Iterate over the rows and extract data
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next(); // Skip header row

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            String brandName = getCellValue(row.getCell(0)); // Assuming name is in the first column

            // Create Brand entity
            Brand brand = new Brand();
            brand.setName(brandName);

            // Persist the brand
            entityManager.persist(brand);
        }

        // Close resources
        workbook.close();
        inputStream.close();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
