package com.example.dvlacme;

import com.example.dvlacme.domain.Record;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTest {

    private Record record;

    @Before
    public void init() {
        record = DataFactory.makeReferenceRecord1();
    }

    @Test
    public void testMutationCorrect() {
        assertTrue(record.isMutationCorrect());
    }

    @Test
    public void testMutationIncorrect() {
        record.setEndBalance(record.getEndBalance() + 1);
        assertFalse(record.isMutationCorrect());
    }
}
