package com.mavharsha.ecom.model;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ZipCode implements Comparable<ZipCode>{
    
    private final int code;
    public static final String errorZipCodeFiveDigits = "ZipCode should be 5 digits positive integer.";

    public ZipCode(int code) {
        validateZipCode(code);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * Validates the in zip code
     * @param code A 5 digit positive integer
     * @throws IllegalArgumentException when the number is not a 5 digit positive number
     */
    private void validateZipCode(int code) throws IllegalArgumentException {
        Preconditions.checkArgument(code > 9999 && code < 100000, errorZipCodeFiveDigits);
    }

    /**
     * @param zipCode
     * @return 0 when both objects are equal,
     *         > 0 when current is greater than zipCode
     *         < 0 when current is smaller than zipCode
     */
    public int compareTo(ZipCode zipCode) {
        return Integer.compare(this.code, zipCode.code);
    }
}
