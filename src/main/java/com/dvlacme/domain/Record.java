package com.dvlacme.domain;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Record {

    @Id
    @GeneratedValue
    private long id;

    @Column(unique=true, nullable = false)
    @NotNull(message = "Reference cannot be null")
    private Long reference;

    @Column(nullable = false)
    @NotNull(message = "Accountnumber cannot be null")
    private String accountNumber;

    private String description;

    @Column(nullable = false)
    @NotNull(message = "Startbalance cannot be null")
    private BigDecimal startBalance;

    @Column(nullable = false)
    @NotNull(message = "Mutation cannot be null")
    private BigDecimal mutation;

    @Column(nullable = false)
    @NotNull(message = "Endbalance cannot be null")
    private BigDecimal endBalance;

    public long getId() {
        return id;
    }

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

    public BigDecimal getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(BigDecimal startBalance) {
        this.startBalance = startBalance;
    }

    public BigDecimal getMutation() {
        return mutation;
    }

    public void setMutation(BigDecimal mutation) {
        this.mutation = mutation;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    @AssertTrue(message = "Mutation is not correct")
    public boolean isMutationCorrect() {
        return startBalance != null && mutation != null && endBalance != null &&
                startBalance.add(mutation).compareTo(endBalance) == 0;
    }
}
