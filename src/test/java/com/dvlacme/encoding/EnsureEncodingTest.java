package com.dvlacme.encoding;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EnsureEncodingTest {

    private EnsureEncoding ensureEncoding = new EnsureEncoding();

    @Test
    public void testXml() throws IOException {
        final File file = new File(getClass().getClassLoader().getResource("records.xml").getFile());
        Charset charset = ensureEncoding.determineCharset(Files.readAllBytes(file.toPath()));
        Assert.assertEquals(StandardCharsets.UTF_8, charset);
    }

    @Test
    public void testCsv() throws Exception {
        final File file = new File(getClass().getClassLoader().getResource("records.csv").getFile());
        Charset charset = ensureEncoding.determineCharset(Files.readAllBytes(file.toPath()));
        Assert.assertEquals(StandardCharsets.ISO_8859_1, charset);
    }
}