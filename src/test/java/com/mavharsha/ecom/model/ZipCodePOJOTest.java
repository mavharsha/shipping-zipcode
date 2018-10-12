package com.mavharsha.ecom.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.*;

public class ZipCodePOJOTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private ZipCode zipCode;

    @Before
    public void setUp() throws Exception {
        zipCode = new ZipCode(10000);
    }

    @After
    public void tearDown() throws Exception {
        zipCode = null;
    }

    /*
    * Exception Cases
    * */
    @Test
    public void testShouldThrowExceptionIfZipCodeIsNotFiveDigits() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ZipCode.errorZipCodeFiveDigits);
        zipCode = new ZipCode(1);
        zipCode = new ZipCode(9999);
        zipCode = new ZipCode(-99999);
        zipCode = new ZipCode(100000);
    }


    @Test
    public void testShouldReturnCorrectZipCode() {
        assertThat(zipCode.getCode()).as("Should return 10000.").isEqualTo(10000);
        zipCode = new ZipCode(95670);
        assertThat(zipCode.getCode()).as("Should return 95670.").isEqualTo(95670);
        zipCode = new ZipCode(99999);
        assertThat(zipCode.getCode()).as("Should return 99999.").isEqualTo(99999);
    }

    @Test
    public void testShouldValidateZipCodeComparable() {
        ZipCode lowerRange = new ZipCode(10000);
        ZipCode upperRange = new ZipCode(10001);
        assertThat(lowerRange.compareTo(upperRange)).as("Should return -1.").isEqualTo(-1);
        assertThat(upperRange.compareTo(lowerRange)).as("Should return 1.").isEqualTo(1);
        assertThat(upperRange.compareTo(upperRange)).as("Should return 0 as both are equal.").isEqualTo(0);
    }

}