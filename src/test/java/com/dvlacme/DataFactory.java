package com.dvlacme;

import com.dvlacme.domain.Record;

import java.math.BigDecimal;

public class DataFactory {

    public static final Long REFERENCE_1 = 163817l;
    public static final String ACCOUNT_NUMBER_1 = "NL46ABNA0625805417";
    public static final String DESCRIPTION_1 = "Candy for Erik de Vries";
    public static final BigDecimal START_BALANCE_1 = BigDecimal.valueOf(68.95);
    public static final BigDecimal MUTATION_1 = BigDecimal.valueOf(9.92);
    public static final BigDecimal  END_BALANCE_1 = BigDecimal.valueOf(78.87);

    public static final Long REFERENCE_2 = 117747l;
    public static final String ACCOUNT_NUMBER_2 = "NL69ABNA0433647324";
    public static final String DESCRIPTION_2 = "Clothes for Willem Dekker";
    public static final BigDecimal START_BALANCE_2 = BigDecimal.valueOf(5429);
    public static final BigDecimal MUTATION_2 = BigDecimal.valueOf(-939);
    public static final BigDecimal  END_BALANCE_2 = BigDecimal.valueOf(6368);

    public static final Long REFERENCE_3 = 153938l;
    public static final String ACCOUNT_NUMBER_3 = "NL32RABO0195610843";
    public static final String DESCRIPTION_3 = "Subscription for Jan King";
    public static final BigDecimal START_BALANCE_3 = BigDecimal.valueOf(14.83);
    public static final BigDecimal MUTATION_3 = BigDecimal.valueOf(-33.47);
    public static final BigDecimal  END_BALANCE_3 = BigDecimal.valueOf(-18.64);

    public static Record makeRecord(Long reference, String accountNumber, String description, BigDecimal startBalance, BigDecimal mutation, BigDecimal endBalance) {
        Record record = new Record();
        record.setReference(reference);
        record.setAccountNumber(accountNumber);
        record.setDescription(description);
        record.setStartBalance(startBalance);
        record.setMutation(mutation);
        record.setEndBalance(endBalance);
        return record;
    }

    public static Record makeReferenceRecord1() {
        return makeRecord(REFERENCE_1, ACCOUNT_NUMBER_1, DESCRIPTION_1, START_BALANCE_1, MUTATION_1, END_BALANCE_1);
    }

    public static Record makeReferenceRecord2() {
        return makeRecord(REFERENCE_2, ACCOUNT_NUMBER_2, DESCRIPTION_2, START_BALANCE_2, MUTATION_2, END_BALANCE_2);
    }

    public static Record makeReferenceRecord3() {
        return makeRecord(REFERENCE_3, ACCOUNT_NUMBER_3, DESCRIPTION_3, START_BALANCE_3, MUTATION_3, END_BALANCE_3);
    }
}
