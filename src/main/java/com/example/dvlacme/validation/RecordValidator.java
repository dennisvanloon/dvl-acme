package com.example.dvlacme.validation;

import com.example.dvlacme.domain.ErrorRecord;
import com.example.dvlacme.domain.Record;
import com.example.dvlacme.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private RecordService recordService;

    private Validator validator;

    @Autowired
    public RecordValidator(RecordService recordService) {
        this.recordService = recordService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Collection<ErrorRecord> validateRecords(Collection<Record> records) {

        Set<ErrorRecord> result = new HashSet<>();
        Set<Long> duplicateReferences = determineDuplicateReferences(records);

        for (Record record: records) {
            Set<String> errors = new HashSet<>();

            Set<ConstraintViolation<Record>> violations = validator.validate(record);
            for (ConstraintViolation<Record> violation : violations) {
                errors.add(violation.getMessage());
            }

            if (duplicateReferences.contains(record.getReference())) {
                errors.add("Reference is duplicate within this set");
            }

            if (recordService.exists(record.getReference())) {
                errors.add("Reference was used during a previous upload");
            }

            if (!errors.isEmpty()) {
                result.add(new ErrorRecord(record, errors));
            }
        }

        return result;
    }

    private Set<Long> determineDuplicateReferences(Collection<Record> records) {
        Map<Long, Long> countPerReference = records.stream()
                .collect(Collectors.groupingBy(record -> record.getReference(), Collectors.counting()));
        return countPerReference.entrySet().stream()
                .filter(entry -> entry.getValue() > 1 )
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
    }

}
