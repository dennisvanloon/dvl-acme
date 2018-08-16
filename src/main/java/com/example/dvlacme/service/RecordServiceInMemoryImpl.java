package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;
import com.example.dvlacme.repository.RecordRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecordServiceInMemoryImpl implements RecordService {

    private RecordRepository recordRepository;

    @Autowired
    public RecordServiceInMemoryImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public boolean exists(Long reference) {
        return recordRepository.existsById(reference);
    }

    @Override
    public Record add(Record record) {
        return recordRepository.save(record);
    }

    @Override
    public void addAll(Collection<Record> records) {
        recordRepository.saveAll(records);
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
