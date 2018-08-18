package com.dvlacme.parser;

import com.dvlacme.domain.Record;
import com.dvlacme.exception.AcmeApplicationException;
import org.junit.Test;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CsvParserImplTest {

    private CsvParserImpl parser = new CsvParserImpl();

    @Test
    public void testProcessingOfCorrectFile() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records.csv").getFile());
        final InputStream input = new FileInputStream(file);
        final List<Record> records = parser.process(input, StandardCharsets.ISO_8859_1);
        assertEquals(10, records.size());

        Record record = records.get(0);
        assertEquals(157637L, record.getReference().longValue());
        assertEquals("NL93ABNA0585619023", record.getAccountNumber());
        assertEquals("Clothes for Erik de Vries", record.getDescription());
        assertEquals(new BigDecimal("73.34"), record.getStartBalance());
        assertEquals(new BigDecimal("-28.79"), record.getMutation());
        assertEquals(new BigDecimal("44.55"), record.getEndBalance());
    }

    @Test(expected = AcmeApplicationException.class)
    public void testProcessingOfIncorrectFile() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records-incorrect.csv").getFile());
        final InputStream input = new FileInputStream(file);
        parser.process(input, StandardCharsets.ISO_8859_1);
    }

}
