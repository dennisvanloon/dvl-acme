package com.dvlacme.parser;

import com.dvlacme.domain.Record;
import com.dvlacme.exception.AcmeApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

@Service
public class StaxParserImpl implements FileParser {

    private final Logger LOG = LoggerFactory.getLogger(StaxParserImpl.class);

    @Value("${parser.error.xml}")
    private String parseError;

    private static final String RECORD = "record";
    private static final String REFERENCE = "reference";
    private static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String DESCRIPTION = "description";
    private static final String START_BALANCE = "startBalance";
    private static final String MUTATION = "mutation";
    private static final String END_BALANCE= "endBalance";

    public List<Record> process(InputStream inputStream,  Charset charset) {
        List<Record> records = new ArrayList<>();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream, charset.name());
            Record record = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals(RECORD)) {
                        record = new Record();
                        // Read the attributes from this tag
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(REFERENCE)) {
                                record.setReference(new Long(attribute.getValue()));
                            }
                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals(ACCOUNT_NUMBER)) {
                            event = eventReader.nextEvent();
                            record.setAccountNumber(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart().equals(DESCRIPTION)) {
                        event = eventReader.nextEvent();
                        record.setDescription(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(START_BALANCE)) {
                        event = eventReader.nextEvent();
                        record.setStartBalance(new BigDecimal(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(MUTATION)) {
                        event = eventReader.nextEvent();
                        record.setMutation(new BigDecimal(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(END_BALANCE)) {
                        event = eventReader.nextEvent();
                        record.setEndBalance(new BigDecimal(event.asCharacters().getData()));
                        continue;
                    }
                }
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(RECORD)) {
                        records.add(record);

                    }
                }
            }
        } catch (XMLStreamException | NumberFormatException e) {
            LOG.error("", e);
            throw new AcmeApplicationException(parseError);
        }
        return records;
    }

}