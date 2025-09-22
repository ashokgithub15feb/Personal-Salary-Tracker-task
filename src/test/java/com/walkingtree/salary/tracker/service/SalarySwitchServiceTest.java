package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.dto.SalarySwitchDTO;
import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalarySwitchServiceTest {

    @Mock
    private SalaryRecordRepository repository;

    private SalarySwitchService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SalarySwitchService(repository);
    }

    @Test
    public void testAddNewSalaryRecord_Success() {
        SalarySwitchDTO dto = new SalarySwitchDTO();
        dto.setCompany("CompanyA");
        dto.setCurrency("USD");
        dto.setYear(2025);
        dto.setSalaryComponent("Fixed CTC");
        dto.setSalaryType("Fixed CTC");
        dto.setAmount(new BigDecimal("120000"));

        SalaryRecord savedRecord = new SalaryRecord();
        savedRecord.setCompany("CompanyA");
        savedRecord.setCurrency("USD");
        savedRecord.setYear(2025);
        savedRecord.setSalaryComponent("Fixed CTC");
        savedRecord.setSalaryType("Fixed CTC");
        savedRecord.setAmount(new BigDecimal("120000"));

        when(repository.save(any(SalaryRecord.class))).thenReturn(savedRecord);

        SalaryRecord result = service.addNewSalaryRecord(dto);

        assertNotNull(result);
        assertEquals("CompanyA", result.getCompany());
        assertEquals(dto.getAmount(), result.getAmount());
        verify(repository, times(1)).save(any(SalaryRecord.class));
    }

    @Test
    public void testAddNewSalaryRecord_NullDtoThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            service.addNewSalaryRecord(null);
        });
    }
}

