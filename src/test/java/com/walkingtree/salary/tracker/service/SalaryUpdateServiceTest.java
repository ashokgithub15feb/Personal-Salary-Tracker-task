package com.walkingtree.salary.tracker.service;

import com.walkingtree.salary.tracker.model.SalaryRecord;
import com.walkingtree.salary.tracker.repository.SalaryRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalaryUpdateServiceTest {

    @Mock
    private SalaryRecordRepository repository;

    private SalaryUpdateService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new SalaryUpdateService(repository);
    }

    @Test
    public void testUpdateSalaryHike_Success() {
        SalaryRecord rec1 = new SalaryRecord();
        rec1.setAmount(new BigDecimal("1000"));

        SalaryRecord rec2 = new SalaryRecord();
        rec2.setAmount(new BigDecimal("3000"));

        List<SalaryRecord> records = new ArrayList<>();
        records.add(rec1);
        records.add(rec2);

        when(repository.findByYear(2024)).thenReturn(records);
        when(repository.save(any(SalaryRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        int updatedCount = service.updateSalaryHike(2024, new BigDecimal("10"));

        assertEquals(2, updatedCount);
        assertEquals(new BigDecimal("1100.0"), rec1.getAmount());
        assertEquals(new BigDecimal("3300.0"), rec2.getAmount());

        verify(repository, times(2)).save(any(SalaryRecord.class));
    }

    @Test
    public void testUpdateSalaryHike_NoRecords() {
        when(repository.findByYear(2025)).thenReturn(List.of());

        int updatedCount = service.updateSalaryHike(2025, new BigDecimal("15"));

        assertEquals(0, updatedCount);
        verify(repository, never()).save(any());
    }

    @Test
    public void testUpdateSalaryHike_NullPercent() {
        // Depending on implementation, this may throw NullPointerException
        assertThrows(NullPointerException.class, () -> {
            service.updateSalaryHike(2024, null);
        });
    }
}

