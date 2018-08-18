package com.dvlacme.validation;

import com.dvlacme.DataFactory;
import com.dvlacme.domain.ErrorRecord;
import com.dvlacme.domain.Record;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecordValidatorTest {

    private RecordValidator validator;

    private Record record1;

    private Record record2;

    private Record record3;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        validator = new RecordValidator();

        record1 = DataFactory.makeReferenceRecord1();  //record with correct mutation
        record2 = DataFactory.makeReferenceRecord2();  //record with incorrect mutation
        record3 = DataFactory.makeReferenceRecord3();  //record with correct mutation
    }

    @Test
    public void testCorrectSetOfRecords() {
        final Collection<ErrorRecord> errorRecords = validator.validateRecords(Arrays.asList(record1, record3));
        assertEquals(0, errorRecords.size());
    }

    @Test
    public void testSetOfRecordsWithOneIncorrectMutation() {
        final Collection<ErrorRecord> errorRecords = validator.validateRecords(Arrays.asList(record1, record2));
        assertEquals(1, errorRecords.size());

        ErrorRecord errorRecord = errorRecords.iterator().next();
        assertEquals(record2, errorRecord.getRecord());
        assertEquals(1, errorRecord.getErrors().size());
        assertEquals("Mutation is not correct", errorRecord.getErrors().iterator().next());
    }

    @Test
    public void testSetOfRecordsWithDuplicateReference() {
        record1.setReference(DataFactory.REFERENCE_3);
        final Collection<ErrorRecord> errorRecords = validator.validateRecords(Arrays.asList(record1, record3));
        assertEquals(2, errorRecords.size());

        Set<Record> invalidRecords = errorRecords.stream().map(ErrorRecord::getRecord).collect(Collectors.toSet());
        assertEquals(2, invalidRecords.size());
        assertTrue(invalidRecords.containsAll(Arrays.asList(record1, record3)));

        Set<String> errorMessages = errorRecords.stream()
                .map(ErrorRecord::getErrors)
                .flatMap(Collection::stream).collect(Collectors.toSet());
        assertEquals(1, errorMessages.size());
        assertEquals("Reference is duplicate within this set", errorMessages.iterator().next());

    }

    //TODO uitbreiden
}
