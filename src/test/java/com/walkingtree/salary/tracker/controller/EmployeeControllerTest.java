//package com.walkingtree.salary.tracker.controller;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.*;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.*;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(EmployeeController.class)
//public class EmployeeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testGetAllEmployees() throws Exception {
//        mockMvc.perform(get("/api/employees"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void testCreateEmployeeInvalidName() throws Exception {
//        String employeeJson = "{\"name\": \"\",\"role\":\"USER\"}";
//
//        mockMvc.perform(post("/api/employees")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(employeeJson))
//                .andExpect(status().isBadRequest());
//    }
//}
