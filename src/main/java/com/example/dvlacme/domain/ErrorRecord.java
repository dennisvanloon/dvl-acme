package com.example.dvlacme.domain;

import java.util.Set;

public class ErrorRecord {

    private Record record;

    private Set<String> errors;

    public ErrorRecord() {
    }

    public ErrorRecord(Record record, Set<String> errors) {
        this.record = record;
        this.errors = errors;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Set<String> getErrors() {
        return errors;
    }

    public void setErrors(Set<String> errors) {
        this.errors = errors;
    }
}
