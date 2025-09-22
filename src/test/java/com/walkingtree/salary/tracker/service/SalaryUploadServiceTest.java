package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class SalaryUploadServiceTest {

    @Mock
    private SalaryRecordRepository repository;

    private SalaryUploadService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SalaryUploadService(repository);
    }

    // Helper to create Excel file bytes with sheets and rows
    private byte[] createExcelFile() throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet2023 = workbook.createSheet("2023");
            Row header = sheet2023.createRow(0);
            header.createCell(0).setCellValue("Company");
            header.createCell(1).setCellValue("Currency");
            header.createCell(2).setCellValue("Component");
            header.createCell(3).setCellValue("Amount");

            Row row1 = sheet2023.createRow(1);
            row1.createCell(0).setCellValue("CompanyA");
            row1.createCell(1).setCellValue("USD");
            row1.createCell(2).setCellValue("Fixed CTC");
            row1.createCell(3).setCellValue(10000);

            Sheet sheet2024 = workbook.createSheet("2024");
            Row header2 = sheet2024.createRow(0);
            header2.createCell(0).setCellValue("Company");
            header2.createCell(1).setCellValue("Currency");
            header2.createCell(2).setCellValue("Component");
            header2.createCell(3).setCellValue("Amount");

            Row row2 = sheet2024.createRow(1);
            row2.createCell(0).setCellValue("CompanyB");
            row2.createCell(1).setCellValue("INR");
            row2.createCell(2).setCellValue("Bonus");
            row2.createCell(3).setCellValue(50000);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return bos.toByteArray();
        }
    }

    @Test
    public void testUploadSalary_ValidFile() throws Exception {
        byte[] excelBytes = createExcelFile();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "salary.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new ByteArrayInputStream(excelBytes)
        );

        service.uploadSalary(multipartFile);

        verify(repository, times(2)).save(any());
    }

    @Test
    public void testUploadSalary_InvalidSheetName() throws Exception {
        // Create Excel file with invalid sheet names
        try (Workbook workbook = new XSSFWorkbook()) {
            workbook.createSheet("NotAYear");
            byte[] bytes;
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                workbook.write(bos);
                bytes = bos.toByteArray();
            }

            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "invalid.xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    new ByteArrayInputStream(bytes));

            service.uploadSalary(file);

            verify(repository, never()).save(any());
        }
    }

    @Test
    public void testUploadSalary_EmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[0]);

        try {
            service.uploadSalary(emptyFile);
        } catch (Exception e) {
            assert true; // Expected exception due to empty file
        }

        verify(repository, never()).save(any());
    }
}
