package com.dvlacme.service;

import com.dvlacme.DataFactory;
import com.dvlacme.domain.ErrorRecord;
import com.dvlacme.domain.Record;
import com.dvlacme.repository.RecordRepository;
import com.dvlacme.validation.RecordValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

public class RecordServiceImplTest {

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private RecordValidator recordValidator;

    private RecordService recordService;

    private Record record1 = DataFactory.makeReferenceRecord1();

    private Record record2 = DataFactory.makeReferenceRecord2();

    private Record record3 = DataFactory.makeReferenceRecord3();

    private ErrorRecord errorRecord1;

    private List<Record> records;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        recordService = new RecordServiceImpl(recordRepository, recordValidator);

        records = new ArrayList<>();
        records.add(record1);
        records.add(record2);
        records.add(record3);

        errorRecord1 = new ErrorRecord(record1, Stream.of("errorString").collect(Collectors.toSet()));
    }

    @Test
    public void testAllValidRecords() {
        Mockito.when(recordValidator.validateRecords(records)).thenReturn(new ArrayList<>());
        Mockito.when(recordRepository.findByReference(record1.getReference())).thenReturn(null);
        Mockito.when(recordRepository.findByReference(record2.getReference())).thenReturn(null);
        Mockito.when(recordRepository.findByReference(record3.getReference())).thenReturn(null);

        Collection<ErrorRecord> errorRecords = recordService.addAll(records);

        Mockito.verify(recordRepository, times(1)).save(record1);
        Mockito.verify(recordRepository, times(1)).save(record2);
        Mockito.verify(recordRepository, times(1)).save(record3);

        assertTrue(errorRecords.isEmpty());
    }

    @Test
    public void testInvalidRecord() {
        Mockito.when(recordValidator.validateRecords(records)).thenReturn(Arrays.asList(errorRecord1));
        Mockito.when(recordRepository.findByReference(record2.getReference())).thenReturn(null);
        Mockito.when(recordRepository.findByReference(record3.getReference())).thenReturn(null);

        Collection<ErrorRecord> errorRecords = recordService.addAll(records);

        Mockito.verify(recordRepository, times(0)).save(record1);
        Mockito.verify(recordRepository, times(1)).save(record2);
        Mockito.verify(recordRepository, times(1)).save(record3);

        assertTrue(errorRecords.size() == 1);
        assertEquals(record1, errorRecords.iterator().next().getRecord());
        assertEquals(1, errorRecords.iterator().next().getErrors().size());
    }

    @Test
    public void testInvalidRecordAndRecordThatAlreadyExists() {
        Mockito.when(recordValidator.validateRecords(records)).thenReturn(Arrays.asList(errorRecord1));
        Mockito.when(recordRepository.findByReference(record2.getReference())).thenReturn(record2);
        Mockito.when(recordRepository.findByReference(record3.getReference())).thenReturn(null);

        Collection<ErrorRecord> errorRecords = recordService.addAll(records);

        Mockito.verify(recordRepository, times(0)).save(record1);
        Mockito.verify(recordRepository, times(0)).save(record2);
        Mockito.verify(recordRepository, times(1)).save(record3);

        assertTrue(errorRecords.size() == 2);
    }
}
