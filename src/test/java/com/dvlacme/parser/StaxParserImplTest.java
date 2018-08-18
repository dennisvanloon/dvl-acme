package com.dvlacme.parser;

import com.dvlacme.domain.Record;
import com.dvlacme.exception.AcmeApplicationException;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StaxParserImplTest {

    private StaxParserImpl parser = new StaxParserImpl();

    @Test
    public void testProcessingOfCorrectFile() throws FileNotFoundException {
        final File file = new File(getClass().getClassLoader().getResource("records.xml").getFile());
        final InputStream input = new FileInputStream(file);
        final List<Record> records = parser.process(input, StandardCharsets.UTF_8);
        assertEquals(10, records.size());

        Record record = records.get(0);
        assertEquals(163817L, record.getReference().longValue());
        assertEquals("NL46ABNA0625805417", record.getAccountNumber());
        assertEquals("Candy for Erik de Vries", record.getDescription());
        assertEquals(new BigDecimal("68.95"), record.getStartBalance());
        assertEquals(new BigDecimal("9.92"), record.getMutation());
        assertEquals(new BigDecimal("78.87"), record.getEndBalance());
    }

    @Test(expected = AcmeApplicationException.class)
    public void testProcessingOfIncorrectFile() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records-incorrect.xml").getFile());
        final InputStream input = new FileInputStream(file);
        parser.process(input, StandardCharsets.UTF_8);
    }

}
