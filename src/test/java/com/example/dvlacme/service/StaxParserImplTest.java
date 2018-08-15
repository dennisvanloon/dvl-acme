package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StaxParserImplTest {

    private StaxParserImpl parser = new StaxParserImpl();

    @Test
    public void testProcessingOfCorrectFile() throws FileNotFoundException {
        final File file = new File(getClass().getClassLoader().getResource("records.xml").getFile());
        final InputStream input = new FileInputStream(file);
        final List<Record> records = parser.process(input);
        assertEquals(10, records.size());

        //TODO test some data

    }

}
