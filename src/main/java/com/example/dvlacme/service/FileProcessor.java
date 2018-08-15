package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileProcessor {

    private final FileParser csvParser;

    private final FileParser staxParser;

    @Autowired
    public FileProcessor(@Qualifier("csvParserImpl") FileParser csvParser, @Qualifier("staxParserImpl") FileParser staxParser) {
        this.csvParser = csvParser;
        this.staxParser = staxParser;
    }

    public List<Record> processFile(String filename, InputStream inputStream) throws IOException, InvalidFileExtensionException {
        String extension = FilenameUtils.getExtension(filename);

        if ("xml".equalsIgnoreCase(extension)) {
            return staxParser.process(inputStream);
        } else if ("csv".equalsIgnoreCase(extension)) {
            return csvParser.process(inputStream);
        }

        throw new InvalidFileExtensionException("Invalid File Extension");
    }

}
