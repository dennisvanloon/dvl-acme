package com.dvlacme.parser;

import com.dvlacme.domain.Record;
import com.dvlacme.exception.AcmeApplicationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParserImpl implements FileParser {

    @Value("${parser.error.csv}")
    private String parseError;

    @Override
    public List<Record> process(InputStream inputStream, Charset charset) throws IOException {
        List<Record> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
        String line;
        try {
            br.readLine();
            while ((line = br.readLine()) != null)   {
                final String[] parts = StringUtils.split(line, ",");
                Record record = new Record();
                record.setReference(new Long(parts[0]));
                record.setAccountNumber(parts[1]);
                record.setDescription(parts[2]);
                record.setStartBalance(new BigDecimal(parts[3]));
                record.setMutation(new BigDecimal(parts[4]));
                record.setEndBalance(new BigDecimal(parts[5]));
                result.add(record);
            }
            br.close();
        } catch (NumberFormatException e) {
            throw new AcmeApplicationException(parseError);
        }
        return result;
    }

}
