package com.dvlacme.repository;

import com.dvlacme.domain.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Long> {

    Record findByReference(Long reference);

}
