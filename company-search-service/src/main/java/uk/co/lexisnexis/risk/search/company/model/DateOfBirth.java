package uk.co.lexisnexis.risk.search.company.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DateOfBirth implements Serializable {
    private static final long serialVersionUID = 123L;

    public int month;
    public int year;
}