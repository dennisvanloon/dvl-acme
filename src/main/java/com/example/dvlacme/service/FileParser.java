package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileParser {

    List<Record> process(InputStream inputStream) throws IOException, InvalidFileContentException;

}
