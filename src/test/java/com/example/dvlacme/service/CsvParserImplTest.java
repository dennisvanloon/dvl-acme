package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CsvParserImplTest {

    private CsvParserImpl parser = new CsvParserImpl();

    @Test
    public void testProcessingOfCorrectFile() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records.csv").getFile());
        final InputStream input = new FileInputStream(file);
        final List<Record> records = parser.process(input);
        assertEquals(10, records.size());

        //TODO test some data and more scenarios
    }

}
