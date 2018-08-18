package com.dvlacme.domain;

import com.dvlacme.DataFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecordTest {

    private Record record; // a record that is correct

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
        record.setEndBalance(record.getEndBalance().add(BigDecimal.ONE));
        assertFalse(record.isMutationCorrect());
    }
}
