package com.mavharsha.ecom.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ZipCodeRangePOJOTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private ZipCodeRange zipCodeRange, anotherZipCodeRange;

    @Before
    public void setUp() {
        zipCodeRange = new ZipCodeRange(10000, 10000);
    }

    @After
    public void tearDown(){
        zipCodeRange = null;
    }

    @Test
    public void testShouldThrowExceptionWhenLowerBoundIsGreaterThanUpperBound() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ZipCodeRange.errorZipCodeLowerGreaterThanUpper);
        zipCodeRange = new ZipCodeRange(10001, 10000);
        zipCodeRange = new ZipCodeRange(99999, 99998);
    }

    @Test
    public void testShouldThrowExceptionWhenNullIsPassedToIsZipRangeMergeble() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("range is marked @NonNull but is null");
        zipCodeRange.isRangeMergeable(null);
    }

    @Test
    public void testShouldValidateZipCodeRangeComparable() throws IOException {
        anotherZipCodeRange = new ZipCodeRange(10001, 10004);
        assertThat(zipCodeRange.compareTo(anotherZipCodeRange)).isLessThan(0);
        assertThat(anotherZipCodeRange.compareTo(anotherZipCodeRange)).isEqualTo(0);
        assertThat(anotherZipCodeRange.compareTo(zipCodeRange)).isGreaterThan(0);
    }

    @Test
    public void testShouldCheckIfZipCodeExistsInRange() {
        assertThat(zipCodeRange.isZipCodeInRange(new ZipCode(10000))).isTrue();
        zipCodeRange = new ZipCodeRange(10000, 10006);
        assertThat(zipCodeRange.isZipCodeInRange(new ZipCode(10004))).isTrue();
        assertThat(zipCodeRange.isZipCodeInRange(new ZipCode(95670))).isFalse();
        zipCodeRange = new ZipCodeRange(95669, 95670);
        assertThat(zipCodeRange.isZipCodeInRange(new ZipCode(95670))).isTrue();
    }


    @Test
    public void testShouldValidateIsZipCodeMergeable() {

        assertThat(zipCodeRange.isRangeMergeable(new ZipCodeRange(10000, 10000)))
                .as("When both ranges are equal, ranges should be mergeable.").isTrue();
        zipCodeRange = new ZipCodeRange(95670, 96670);
        assertThat(zipCodeRange.isRangeMergeable(new ZipCodeRange(96000, 96300)))
                .as("When range overlaps completely, ranges should be mergeable.").isTrue();
        assertThat(zipCodeRange.isRangeMergeable(new ZipCodeRange(96000, 97300)))
                .as("When range overlaps, ranges should be mergeable.").isTrue();

        assertThat(zipCodeRange.isRangeMergeable(new ZipCodeRange(76000, 77300)))
                .as("When range don't overlaps, ranges shouldn't be mergeable.").isFalse();
        assertThat(zipCodeRange.isRangeMergeable(new ZipCodeRange(12345, 54321)))
                .as("When range don't overlaps, ranges shouldn't be mergeable.").isFalse();
    }


    @Test
    public void testShouldValidateMergeRanges() {

        /* When the ranges are identical */
        anotherZipCodeRange = new ZipCodeRange(10000, 10000);
        ZipCodeRange returnedRange = zipCodeRange.mergeRange(anotherZipCodeRange);
        assertThat(returnedRange.getLowerRange().compareTo(zipCodeRange.getLowerRange()) == 0).as("When the ranges are same.").isTrue();
        assertThat(returnedRange.getUpperRange().compareTo(zipCodeRange.getUpperRange()) == 0).as("When the ranges are same.").isTrue();
        assertThat(returnedRange.getLowerRange().compareTo(anotherZipCodeRange.getLowerRange()) == 0).as("When the ranges are same.").isTrue();
        assertThat(returnedRange.getUpperRange().compareTo( anotherZipCodeRange.getUpperRange()) == 0).as("When the ranges are same.").isTrue();


        /* One range exists in another. */
        zipCodeRange = new ZipCodeRange(94200, 94299);
        anotherZipCodeRange = new ZipCodeRange(94226, 94250);

        returnedRange = zipCodeRange.mergeRange(anotherZipCodeRange);
        assertThat(returnedRange.getLowerRange().compareTo(zipCodeRange.getLowerRange()) == 0).as("When one range exists in another.").isTrue();
        assertThat(returnedRange.getUpperRange().compareTo(zipCodeRange.getUpperRange()) == 0).as("When one range exists in another.").isTrue();

        returnedRange = anotherZipCodeRange.mergeRange(zipCodeRange);
        assertThat(returnedRange.getLowerRange().compareTo(zipCodeRange.getLowerRange()) == 0).as("When one range exists in another.").isTrue();
        assertThat(returnedRange.getUpperRange().compareTo(zipCodeRange.getUpperRange()) == 0).as("When one range exists in another.").isTrue();

        /* When there is an overlap between two ranges.*/
        zipCodeRange = new ZipCodeRange(94200, 94299);
        anotherZipCodeRange = new ZipCodeRange(94226, 94399);

        returnedRange = zipCodeRange.mergeRange(anotherZipCodeRange);
        assertThat(returnedRange.getLowerRange().compareTo(zipCodeRange.getLowerRange()) == 0).as("When one range exists in another.").isTrue();
        assertThat(returnedRange.getUpperRange().compareTo(anotherZipCodeRange.getUpperRange()) == 0).as("When one range exists in another.").isTrue();

        returnedRange = anotherZipCodeRange.mergeRange(zipCodeRange);
        assertThat(returnedRange.getLowerRange().compareTo(zipCodeRange.getLowerRange()) == 0).as("When one range exists in another.").isTrue();
        assertThat(returnedRange.getUpperRange().compareTo(anotherZipCodeRange.getUpperRange()) == 0).as("When one range exists in another.").isTrue();
    }
}