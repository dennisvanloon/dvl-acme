package com.dvlacme.service;

import com.dvlacme.encoding.EnsureEncoding;
import com.dvlacme.exception.AcmeApplicationException;
import com.dvlacme.parser.FileParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;

public class FileProcessorTest {

    @Mock
    private EnsureEncoding ensureEncoding;

    @Mock
    private FileParser csvParser;

    @Mock
    private FileParser staxParser;

    private FileProcessor fileProcessor;

    private Charset charset = StandardCharsets.UTF_8;

    private byte[] testBytes = "teststring".getBytes();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fileProcessor = new FileProcessor(csvParser, staxParser, ensureEncoding);
    }

    @Test
    public void testAcceptXmlFile() throws IOException {
        final InputStream input = new ByteArrayInputStream(testBytes);

        Mockito.when(ensureEncoding.determineCharset(any(byte[].class))).thenReturn(charset);
        fileProcessor.processFile("records.xml", input);

        Mockito.verify(staxParser, Mockito.times(1)).process(any(InputStream.class), same(charset));
        Mockito.verifyNoMoreInteractions(csvParser);
    }

    @Test
    public void testAcceptCsvFile() throws IOException {
        final InputStream input = new ByteArrayInputStream(testBytes);

        Mockito.when(ensureEncoding.determineCharset(any(byte[].class))).thenReturn(charset);
        fileProcessor.processFile("records.csv", input);

        Mockito.verify(csvParser, Mockito.times(1)).process(any(InputStream.class), same(charset));
        Mockito.verifyNoMoreInteractions(staxParser);
    }

    @Test(expected = AcmeApplicationException.class)
    public void testRejectTxtFile() throws IOException {
        final InputStream input = new ByteArrayInputStream(testBytes);
        fileProcessor.processFile("records.txt", input);
    }

    @Test(expected = AcmeApplicationException.class)
    public void testRejectUnknownCharset() throws IOException {
        final InputStream input = new ByteArrayInputStream(testBytes);

        Mockito.when(ensureEncoding.determineCharset(any(byte[].class))).thenReturn(null);
        fileProcessor.processFile("records.txt", input);
    }
}
