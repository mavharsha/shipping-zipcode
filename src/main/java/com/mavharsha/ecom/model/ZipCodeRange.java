package com.mavharsha.ecom.model;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Comparator;

@ToString
@EqualsAndHashCode
public class ZipCodeRange implements Comparable<ZipCodeRange> {
    private final ZipCode lowerRange;
    private final ZipCode upperRange;

    public static final String errorZipCodeLowerGreaterThanUpper = "UpperRange cannot be greater than LowerRange.";

    public ZipCodeRange(int lowerRange, int upperRange) {
        this(new ZipCode(lowerRange), new ZipCode(upperRange));
    }

    public ZipCodeRange(ZipCode lowerRange, ZipCode upperRange) {
        validateRanges(lowerRange.getCode(), upperRange.getCode());
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
    }

    public ZipCode getLowerRange() {
        return lowerRange;
    }

    public ZipCode getUpperRange() {
        return upperRange;
    }


    /**
     * @param lowerRange  lowerBound of the ZipCodeRange
     * @param upperRange  upperBound of the ZipCodeRange
     * @throws IllegalArgumentException if lowerRange is greater than upperRange
     */
    private void validateRanges(int lowerRange, int upperRange) throws IllegalArgumentException {
        Preconditions.checkArgument(lowerRange <= upperRange, errorZipCodeLowerGreaterThanUpper);
    }


    /**
     * @param range A ZipCodeRange
     * @return true if both zipCodeRanges overlap, else false
     */
    public boolean isRangeMergeable(@NonNull ZipCodeRange range) {
        if (this.compareTo(range) == 0) {
            return true;
        }
        return isZipCodeInRange(range.getLowerRange()) || isZipCodeInRange(range.getUpperRange());
    }

    /**
     * @param range1 A ZipCodeRange
     * @param range2 A ZipCodeRange
     * @return true if both zipCodeRanges overlap, else false
     */
    public boolean areRangesMergeable(@NonNull ZipCodeRange range1, @NonNull ZipCodeRange range2) {
        if (range1.compareTo(range2) == 0) {
            return true;
        }
        return range1.isZipCodeInRange(range2.getLowerRange()) || range1.isZipCodeInRange(range2.getUpperRange())
                || range2.isZipCodeInRange(range1.getLowerRange()) || range2.isZipCodeInRange(range1.getUpperRange());
    }

    /**
     * @param code A ZipCode
     * @return true if code is in current object range, else false
     */
    public boolean isZipCodeInRange(@NonNull ZipCode code) {
        return (code.compareTo(this.getLowerRange()) == 0 || code.compareTo(this.getLowerRange()) > 0) &&
                (code.compareTo(this.getUpperRange()) == 0 || code.compareTo(this.getUpperRange()) < 0);
    }


    /**
     * @param zipCodeRange A ZipCodeRange object that is to be merged to current(this) object.
     * @return new ZipCodeRange if current and zipCodeRange object can be merged.
     */
    public ZipCodeRange mergeRange(@NonNull ZipCodeRange zipCodeRange) {
        if (!this.areRangesMergeable(this, zipCodeRange)) {
            throw new IllegalArgumentException("Ranges cannot be merged.");
        }

        if (this.compareTo(zipCodeRange) == 0) {
            return new ZipCodeRange(this.getLowerRange(), this.getUpperRange());
        } else if (this.compareTo(zipCodeRange) < 0) {
            if (this.isZipCodeInRange(zipCodeRange.getLowerRange()) && this.isZipCodeInRange(zipCodeRange.getUpperRange())) {
                return new ZipCodeRange(this.getLowerRange(), this.getUpperRange());
            } else {
                return new ZipCodeRange(this.getLowerRange(), zipCodeRange.getUpperRange());
            }
        } else {
            if (zipCodeRange.isZipCodeInRange(this.getLowerRange()) && zipCodeRange.isZipCodeInRange(this.getUpperRange())) {
                return new ZipCodeRange(zipCodeRange.getLowerRange(), zipCodeRange.getUpperRange());
            } else {
                return new ZipCodeRange(zipCodeRange.getLowerRange(), this.getUpperRange());
            }
        }
    }

    /**
     * @param range ZipCodeRage comparing to the current object
     * @return returns 0 when both are equal,
     * > 0 when this is lesser than range and
     * < 0 when this is greater than range
     */
    public int compareTo(ZipCodeRange range) {
        return Comparator.comparing(ZipCodeRange::getLowerRange)
                .thenComparing(ZipCodeRange::getUpperRange)
                .compare(this, range);
    }
}
