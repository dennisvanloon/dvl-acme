package com.dvlacme.service;

import com.dvlacme.domain.ErrorRecord;
import com.dvlacme.domain.Record;

import java.util.Collection;

public interface RecordService {

    Collection<ErrorRecord> addAll(Collection<Record> records);

    Collection<Record> getAll();

    void clear();

}
