package com.dvlacme.validation;

import com.dvlacme.domain.ErrorRecord;
import com.dvlacme.domain.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger LOG = LoggerFactory.getLogger(RecordValidator.class);

    private Validator validator;

    @Autowired
    public RecordValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Collection<ErrorRecord> validateRecords(Collection<Record> records) {

        LOG.info(String.format("Validating %d records", records.size()));

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

            if (!errors.isEmpty()) {
                result.add(new ErrorRecord(record, errors));
            }
        }

        LOG.info(String.format("Found %d error records", result.size()));
        return result;
    }

    private Set<Long> determineDuplicateReferences(Collection<Record> records) {
        Map<Long, Long> countPerReference = records.stream()
                .collect(Collectors.groupingBy(Record::getReference, Collectors.counting()));
        return countPerReference.entrySet().stream()
                .filter(entry -> entry.getValue() > 1 )
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

}
