package com.dvlacme.service;

import com.dvlacme.domain.ErrorRecord;
import com.dvlacme.domain.Record;
import com.dvlacme.repository.RecordRepository;
import com.dvlacme.validation.RecordValidator;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecordServiceImpl implements RecordService {

    private final Logger LOG = LoggerFactory.getLogger(RecordServiceImpl.class);

    private RecordRepository recordRepository;

    private final RecordValidator recordValidator;

    @Value("${save.error.reference.exists}")
    private String referenceExistsError;

    @Autowired
    public RecordServiceImpl(RecordRepository recordRepository, RecordValidator recordValidator) {
        this.recordRepository = recordRepository;
        this.recordValidator = recordValidator;
    }

    @Override
    public Collection<ErrorRecord> addAll(Collection<Record> records) {
        LOG.info(String.format("Received %d records to store", records.size()));

        Collection<ErrorRecord> errorRecords = recordValidator.validateRecords(records);
        records.removeAll(errorRecords.stream().map(ErrorRecord::getRecord).collect(Collectors.toSet()));

        Map<Record, ErrorRecord> errorRecordsByRecord = errorRecords.stream()
                .collect(Collectors.toMap(ErrorRecord::getRecord, Function.identity()));

        for (Record record : records) {
            if (recordRepository.findByReference(record.getReference()) != null) {
                errorRecordsByRecord.put(record, new ErrorRecord(record, Stream.of(referenceExistsError).collect(Collectors.toSet())));
            } else {
                recordRepository.save(record);
            }
        }

        LOG.info(String.format("Stored %d records", records.size()));
        return errorRecordsByRecord.values();
    }

    @Override
    public Collection<Record> getAll() {
        return IterableUtils.toList(recordRepository.findAll());
    }

    @Override
    public void clear() {
        recordRepository.deleteAll();
    }

}
