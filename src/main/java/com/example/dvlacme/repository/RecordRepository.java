package com.example.dvlacme.repository;


import com.example.dvlacme.domain.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Long> { }
