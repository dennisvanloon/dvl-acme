package com.example.dvlacme.parser;

import com.example.dvlacme.domain.Record;
import org.springframework.stereotype.Service;

import java.io.InputStream;
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

    static final String RECORD = "record";
    static final String REFERENCE = "reference";
    static final String ACCOUNT_NUMBER = "accountNumber";
    static final String DESCRIPTION = "description";
    static final String START_BALANCE = "startBalance";
    static final String MUTATION = "mutation";
    static final String END_BALANCE= "endBalance";

    public List<Record> process(InputStream inputStream) throws InvalidFileContentException {
        List<Record> records = new ArrayList<>();
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(inputStream);
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
                                record.setReference(new Integer(attribute.getValue()));
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
                        record.setStartBalance(new Double(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(MUTATION)) {
                        event = eventReader.nextEvent();
                        record.setMutation(new Double(event.asCharacters().getData()));
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(END_BALANCE)) {
                        event = eventReader.nextEvent();
                        record.setEndBalance(new Double(event.asCharacters().getData()));
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
            //TODO log to logger
            throw new InvalidFileContentException("Found invalid content during xml processing");
        }
        return records;
    }

}