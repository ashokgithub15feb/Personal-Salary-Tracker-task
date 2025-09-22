package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;

@Service
public class SalaryUploadService {

    private final SalaryRecordRepository repository;

    public SalaryUploadService(SalaryRecordRepository repository) {
        this.repository = repository;
    }

    public void uploadSalary(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                int year;
                try {
                    year = Integer.parseInt(sheetName);
                } catch (NumberFormatException e) {
                    continue; // skip non-year sheets
                }

                Iterator<Row> rowIterator = sheet.iterator();
                if (rowIterator.hasNext()) rowIterator.next(); // Skip header row

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    String company = row.getCell(0).getStringCellValue();
                    String currency = row.getCell(1).getStringCellValue();
                    String component = row.getCell(2).getStringCellValue();
                    String salaryType = row.getCell(3).getStringCellValue();
                    BigDecimal amount = BigDecimal.valueOf(row.getCell(4).getNumericCellValue());

                    SalaryRecord record = new SalaryRecord();
                    record.setCompany(company);
                    record.setCurrency(currency);
                    record.setYear(year);
                    record.setSalaryComponent(component);
                    record.setSalaryType(salaryType);
                    record.setAmount(amount);

                    repository.save(record);
                }
            }
        }
    }
}
