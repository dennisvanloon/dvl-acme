package com.example.dvlacme.service;

import com.example.dvlacme.domain.ErrorRecord;
import com.example.dvlacme.domain.Record;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecordValidator {

    private Validator validator;

    public RecordValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Collection<ErrorRecord> validateRecords(Collection<Record> records) {

        Set<ErrorRecord> result = new HashSet<>();
        Set<Integer> duplicateReferences = determineDuplicateReferences(records);

        for (Record record: records) {
            Set<String> errors = new HashSet<>();

            Set<ConstraintViolation<Record>> violations = validator.validate(record);
            for (ConstraintViolation<Record> violation : violations) {
                errors.add(violation.getMessage());
            }

            if (duplicateReferences.contains(record.getReference())) {
                errors.add("Reference is duplicate within this set");
            }

            //TODO check against service if reference already exists

            if (!errors.isEmpty()) {
                result.add(new ErrorRecord(record, errors));
            }
        }

        return result;
    }

    private Set<Integer> determineDuplicateReferences(Collection<Record> records) {
        Map<Integer, Long> countPerReference = records.stream()
                .collect(Collectors.groupingBy(record -> record.getReference(), Collectors.counting()));
        return countPerReference.entrySet().stream()
                .filter(entry -> entry.getValue() > 1 )
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
    }

}
