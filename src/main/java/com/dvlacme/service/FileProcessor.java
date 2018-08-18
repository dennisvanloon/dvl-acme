package com.dvlacme.service;

import com.dvlacme.domain.Record;
import com.dvlacme.encoding.EnsureEncoding;
import com.dvlacme.exception.AcmeApplicationException;
import com.dvlacme.parser.FileParser;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

@Service
public class FileProcessor {

    private enum VALID_EXTENSIONS {
        CSV, XML
    }

    @Value("${file.extension.incorrect}")
    private String fileExtensionIncorrect;

    @Value("${file.encoding.incorrect}")
    private String fileEncodingIncorrect;

    private final EnsureEncoding ensureEncoding;

    private final FileParser csvParser;

    private final FileParser staxParser;

    @Autowired
    public FileProcessor(@Qualifier("csvParserImpl") FileParser csvParser, @Qualifier("staxParserImpl") FileParser staxParser, EnsureEncoding ensureEncoding) {
        this.csvParser = csvParser;
        this.staxParser = staxParser;
        this.ensureEncoding = ensureEncoding;
    }

    public List<Record> processFile(String filename, InputStream inputStream) throws IOException {
        String extension = FilenameUtils.getExtension(filename).toUpperCase();
        if (!EnumUtils.isValidEnum(VALID_EXTENSIONS.class, extension)) {
            throw new AcmeApplicationException(fileExtensionIncorrect);
        }

        final byte[] input = IOUtils.toByteArray(inputStream);
        Charset charset = ensureEncoding.determineCharset(input);
        if (charset == null) {
            throw new AcmeApplicationException(fileEncodingIncorrect);
        }

        if (VALID_EXTENSIONS.XML.name().equals(extension)) {
            return staxParser.process(new ByteArrayInputStream(input), charset);
        }
        return csvParser.process(new ByteArrayInputStream(input), charset);

    }

}
