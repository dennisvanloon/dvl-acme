package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class RecordServiceInMemoryImpl implements RecordService {

    private Map<Integer, Record> recordsByReference;

    public RecordServiceInMemoryImpl() {
        recordsByReference = new HashMap<>();
    }

    @Override
    public boolean exists(Integer reference) {
        return recordsByReference.containsKey(reference);
    }

    @Override
    public void add(Record record) {
        recordsByReference.put(record.getReference(), record);
    }

    @Override
    public void addAll(Collection<Record> records) {
        records.forEach(record -> add(record));
    }

    @Override
    public Collection<Record> getAll() {
        return recordsByReference.values();
    }

    @Override
    public void clear() {
        recordsByReference.clear();
    }
}
