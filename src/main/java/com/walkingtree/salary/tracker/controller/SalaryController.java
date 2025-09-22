package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.service.SalaryUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {

    private final SalaryUploadService uploadService;

    public SalaryController(SalaryUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadSalaryFile(@RequestParam("file") MultipartFile file) {
        try {
            uploadService.uploadSalary(file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
}
