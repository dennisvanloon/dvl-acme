package com.example.dvlacme.service;

import com.example.dvlacme.domain.Record;

import java.util.Collection;

public interface RecordService {

    boolean exists(Long reference);

    Record add(Record record);

    void addAll(Collection<Record> records);

    Collection<Record> getAll();

    void clear();

}
