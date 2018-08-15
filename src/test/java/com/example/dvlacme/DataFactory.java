package com.example.dvlacme;

import com.example.dvlacme.domain.Record;

public class DataFactory {

    public static final Integer REFERENCE_1 = 163817;
    public static final String ACCOUNT_NUMBER_1 = "NL46ABNA0625805417";
    public static final String DESCRIPTION_1 = "Candy for Erik de Vries";
    public static final Double START_BALANCE_1 = 68.95;
    public static final Double MUTATION_1 = 9.92;
    public static final Double  END_BALANCE_1 = 78.87;

    public static final Integer REFERENCE_2 = 117747;
    public static final String ACCOUNT_NUMBER_2 = "NL69ABNA0433647324";
    public static final String DESCRIPTION_2 = "Clothes for Willem Dekker";
    public static final Double START_BALANCE_2 = 5429d;
    public static final Double MUTATION_2 = -939d;
    public static final Double  END_BALANCE_2 = 6368d;

    public static final Integer REFERENCE_3 = 153938;
    public static final String ACCOUNT_NUMBER_3 = "NL32RABO0195610843";
    public static final String DESCRIPTION_3 = "Subscription for Jan King";
    public static final Double START_BALANCE_3 = 14.83;
    public static final Double MUTATION_3 = -33.47;
    public static final Double  END_BALANCE_3 = -18.64;

    public static Record makeRecord(Integer reference, String accountNumber, String description, Double startBalance, Double mutation, Double endBalance) {
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
