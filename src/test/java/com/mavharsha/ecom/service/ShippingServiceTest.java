package com.mavharsha.ecom.service;

import com.mavharsha.ecom.model.ZipCode;
import com.mavharsha.ecom.model.ZipCodeRange;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ShippingServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /* Exceptions */
    @Test
    public void testShouldThrowExceptionWhenFileIsNotFound() throws IOException {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("resource anotherFile.json not found.");
        IShippingService shippingService = new ShippingService("anotherFile.json");
        shippingService.getListZipCodeRanges();
    }

    @Test
    public void testShouldThrowNullPointerExceptionWhenNullIsPassedToCanBeShipped() throws Exception {
        thrown.expect(NullPointerException.class);
        IShippingService shippingService = new ShippingService();
        shippingService.canBeShippedToZipCode(null);
    }

    @Test
    public void testShouldValidateGetListZipCodeRanges() throws Exception {
        IShippingService shippingService = new ShippingService();
        List<ZipCodeRange> expected = Arrays.asList(new ZipCodeRange(94133, 94133),
                new ZipCodeRange(94200, 94299),
                new ZipCodeRange(94226, 94399));
        List<ZipCodeRange> ranges = shippingService.getListZipCodeRanges();
        assertThat(ranges.size()).as("Should return 3").isEqualTo(expected.size());
        for (int index = 0; index < ranges.size(); index++) {
            assertThat(ranges.get(index).equals(expected.get(index))).isTrue();
        }
    }

    @Test
    public void testShouldValidateConsolidateShippingZipRanges() throws Exception {
        IShippingService shippingService = new ShippingService();
        List<ZipCodeRange> expected = Arrays.asList(new ZipCodeRange(94133, 94133),
                new ZipCodeRange(94200, 94399));
        List<ZipCodeRange> ranges = shippingService.getConsolidatedShippableZipCodeRanges();
        assertThat(ranges.size()).as("Should return 2").isEqualTo(expected.size());
        for (int index = 0; index < ranges.size(); index++) {
            assertThat(ranges.get(index).equals(expected.get(index))).isTrue();
        }
    }


    @Test
    public void testShouldValidateCanBeShippedToZipCode() throws Exception {
        IShippingService shippingService = new ShippingService();
        List<ZipCode> expectedToBeDelivered = Arrays.asList(new ZipCode(94133),
                new ZipCode(94200),
                new ZipCode(94226));

        for (int index = 0; index < expectedToBeDelivered.size(); index++) {
            assertThat(shippingService.canBeShippedToZipCode(expectedToBeDelivered.get(index))).as("Can be shipped").isTrue();
        }

        List<ZipCode> expectedToBeFailDeliver = Arrays.asList(new ZipCode(94130),
                new ZipCode(94199),
                new ZipCode(94450));
        for (int index = 0; index < expectedToBeDelivered.size(); index++) {
           assertThat(shippingService.canBeShippedToZipCode(expectedToBeFailDeliver.get(index))).as("Can not be shipped").isFalse();
        }
    }
}