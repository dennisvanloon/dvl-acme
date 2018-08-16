package com.example.dvlacme.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.AssertTrue;

@Entity
public class Record {

    @Id
    private Long reference;

    private String accountNumber;

    private String description;

    private Double startBalance;

    private Double mutation;

    private Double endBalance;

    public Long getReference() {
        return reference;
    }

    public void setReference(Long reference) {
        this.reference = reference;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(Double startBalance) {
        this.startBalance = startBalance;
    }

    public Double getMutation() {
        return mutation;
    }

    public void setMutation(Double mutation) {
        this.mutation = mutation;
    }

    public Double getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(Double endBalance) {
        this.endBalance = endBalance;
    }

    @AssertTrue(message = "Mutation is not correct")
    public boolean isMutationCorrect() {
        return startBalance + mutation == endBalance;
    }
}
