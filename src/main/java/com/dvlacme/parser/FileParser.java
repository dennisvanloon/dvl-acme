package com.dvlacme.parser;

import com.dvlacme.domain.Record;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

public interface FileParser {

    List<Record> process(InputStream inputStream, Charset charset) throws IOException;

}
