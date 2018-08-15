package com.example.dvlacme.service;

import com.example.dvlacme.DataFactory;
import com.example.dvlacme.domain.Record;
import com.example.dvlacme.parser.FileParser;
import com.example.dvlacme.parser.InvalidFileExtensionException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileProcessorTest {

    @Mock
    private FileParser csvParser;

    @Mock
    private FileParser staxParser;

    private List<Record> mockRecords = Arrays.asList(DataFactory.makeReferenceRecord1(), DataFactory.makeReferenceRecord2(), DataFactory.makeReferenceRecord3());

    private FileProcessor fileProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fileProcessor = new FileProcessor(csvParser, staxParser);
    }

    @Test
    public void testAcceptXmlFile() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records.xml").getFile());
        final InputStream input = new FileInputStream(file);

        Mockito.when(staxParser.process(input)).thenReturn(mockRecords);

        final List<Record> records = fileProcessor.processFile("records.xml", input);

        assertEquals(mockRecords, records);
        Mockito.verify(staxParser, Mockito.times(1)).process(input);
        Mockito.verifyNoMoreInteractions(csvParser);
    }

    @Test
    public void testAcceptCsvFile() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records.csv").getFile());
        final InputStream input = new FileInputStream(file);

        Mockito.when(csvParser.process(input)).thenReturn(mockRecords);

        final List<Record> records = fileProcessor.processFile("records.csv", input);

        assertEquals(mockRecords, records);
        Mockito.verify(csvParser, Mockito.times(1)).process(input);
        Mockito.verifyNoMoreInteractions(staxParser);
    }

    @Test(expected = InvalidFileExtensionException.class)
    public void testRejectTxtFile() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records.txt").getFile());
        final InputStream input = new FileInputStream(file);
        fileProcessor.processFile("records.txt", input);
    }
}
