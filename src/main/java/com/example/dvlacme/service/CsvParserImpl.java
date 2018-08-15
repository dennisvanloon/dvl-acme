package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvParserImpl implements FileParser {

    @Override
    public List<Record> process(InputStream inputStream) throws IOException, InvalidFileContentException {
        List<Record> result = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            br.readLine();
            while ((line = br.readLine()) != null)   {
                final String[] parts = StringUtils.split(line, ",");
                Record record = new Record();
                record.setReference(new Integer(parts[0]));
                record.setAccountNumber(parts[1]);
                record.setDescription(parts[2]);
                record.setStartBalance(new Double(parts[3]));
                record.setMutation(new Double(parts[4]));
                record.setEndBalance(new Double(parts[5]));
                result.add(record);
            }
            br.close();
        } catch (NumberFormatException e) {
            //TODO log to logger
            throw new InvalidFileContentException("There was a problem processing the csv file, please check the content");
        }
        return result;
    }

}
