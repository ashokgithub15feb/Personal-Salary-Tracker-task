package com.walkingtree.salary.tracker.controller;

import com.walkingtree.salary.tracker.service.SalaryUploadService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaryController.class)
public class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SalaryUploadService uploadService;

    @Test
    public void testUploadSalaryFile_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "salary.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "dummy".getBytes());

        mockMvc.perform(multipart("/api/salaries/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    public void testUploadSalaryFile_Failure() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "salary.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "dummy".getBytes());

        doThrow(new RuntimeException("Parsing error")).when(uploadService).uploadSalary(Mockito.any());

        mockMvc.perform(multipart("/api/salaries/upload").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to upload file: Parsing error"));
    }

    @Test
    public void testUploadSalaryFile_MissingFileParam() throws Exception {
        mockMvc.perform(multipart("/api/salaries/upload"))
                .andExpect(status().isBadRequest());
    }
}
