package com.mavharsha.ecom.service;

import com.mavharsha.ecom.dao.IZipCodeRangeDAO;
import com.mavharsha.ecom.model.ZipCode;
import com.mavharsha.ecom.model.ZipCodeRange;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class ShippingServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private IZipCodeRangeDAO zipCodeRangeDAO;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShouldThrowNullPointerExceptionWhenNullIsPassedToCanBeShipped() throws Exception {
        thrown.expect(NullPointerException.class);
        IShippingService shippingService = new ShippingService();
        shippingService.canBeShippedToZipCode(null);
    }

    @Test
    public void testShouldThrowExceptionIncaseOfBadDataSource() throws Exception {
        thrown.expect(IOException.class);
        IShippingService shippingService = new ShippingService(zipCodeRangeDAO);
        when(zipCodeRangeDAO.getListOfZipCodeRanges()).thenThrow(IOException.class);
        shippingService.getListZipCodeRanges();
    }

    @Test
    public void testShouldReturnEmptyListWhenDataSourceHasZeroRecords() throws Exception {
        IShippingService shippingService = new ShippingService(zipCodeRangeDAO);
        when(zipCodeRangeDAO.getListOfZipCodeRanges()).thenReturn(new ArrayList<>());

        List<ZipCodeRange> ranges = shippingService.getListZipCodeRanges();
        assertThat(ranges.size()).as("Should return an empty list").isEqualTo(0);
    }


    @Test
    public void testShouldValidateGetListZipCodeRanges() throws Exception {

        IShippingService shippingService = new ShippingService(zipCodeRangeDAO);
        List<ZipCodeRange> expected = Arrays.asList(new ZipCodeRange(94133, 94133),
                new ZipCodeRange(94200, 94299),
                new ZipCodeRange(94226, 94399));
        when(zipCodeRangeDAO.getListOfZipCodeRanges()).thenReturn(expected);

        List<ZipCodeRange> ranges = shippingService.getListZipCodeRanges();
        assertThat(ranges.size()).as("Should return list of size greater than 0").isGreaterThan(0);
        assertThat(ranges.size()).as("Should return list of size equal to 3").isEqualTo(expected.size());
        for (int index = 0; index < ranges.size(); index++) {
            assertThat(ranges.get(index).equals(expected.get(index))).as("" + index).isTrue();
        }
    }

    @Test
    public void testShouldReturnEmptyConsolidatedShippingZipRanges() throws Exception {
        IShippingService shippingService = new ShippingService(zipCodeRangeDAO);
        IShippingService shippingServiceSpy = PowerMockito.spy(shippingService);
        PowerMockito.when(shippingServiceSpy.getListZipCodeRanges()).thenReturn(new ArrayList<>());
        List<ZipCodeRange> ranges = shippingService.getConsolidatedShippableZipCodeRanges();
        assertThat(ranges.size()).as("Should return an empty list").isEqualTo(0);
    }



    @Test
    public void testShouldValidateConsolidateShippingZipRanges() throws Exception {
        IShippingService shippingService = new ShippingService(zipCodeRangeDAO);
        IShippingService shippingServiceSpy = PowerMockito.spy(shippingService);

        List<ZipCodeRange> returnedListOfZipCode = Arrays.asList(new ZipCodeRange(94133, 94133),
                new ZipCodeRange(94200, 94299),
                new ZipCodeRange(94226, 94399));
        List<ZipCodeRange> expected = Arrays.asList(new ZipCodeRange(94133, 94133),
                new ZipCodeRange(94200, 94399));

        PowerMockito.when(shippingServiceSpy.getListZipCodeRanges()).thenReturn(returnedListOfZipCode);
        List<ZipCodeRange> ranges = shippingService.getConsolidatedShippableZipCodeRanges();
        assertThat(ranges.size()).as("Should return list of size greater than 0").isGreaterThan(0);
        assertThat(ranges.size()).as("Should return list of size 2").isEqualTo(expected.size());

        for (int index = 0; index < ranges.size(); index++) {
            assertThat(ranges.get(index).equals(expected.get(index))).isTrue();
        }
    }

    @Test
    public void testShouldReturnFalseWhenTheDataSourceReturnsZeroRecords() throws IOException{
        IShippingService shippingService = new ShippingService(zipCodeRangeDAO);
        IShippingService shippingServiceSpy = PowerMockito.spy(shippingService);


        PowerMockito.when(shippingServiceSpy.getListZipCodeRanges()).thenReturn(new ArrayList<>());
        PowerMockito.when(shippingServiceSpy.getConsolidatedShippableZipCodeRanges()).thenReturn(new ArrayList<>());

        List<ZipCode> expectedToBeDelivered = Arrays.asList();
        List<ZipCode> expectedDeliveriesToFail= Arrays.asList(new ZipCode(94130),
                new ZipCode(94133),
                new ZipCode(94200),
                new ZipCode(94226),
                new ZipCode(94199),
                new ZipCode(94450));
        for (int index = 0; index < expectedToBeDelivered.size(); index++) {
            assertThat(shippingService.canBeShippedToZipCode(expectedDeliveriesToFail.get(index))).as("Can not be shipped").isFalse();
        }
    }

    @Test
    public void testShouldValidateCanBeShippedToZipCode() throws Exception {
        IShippingService shippingService = new ShippingService(zipCodeRangeDAO);
        IShippingService shippingServiceSpy = PowerMockito.spy(shippingService);

        List<ZipCodeRange> returnedListOfZipCode = Arrays.asList(new ZipCodeRange(94133, 94133),
                new ZipCodeRange(94200, 94299),
                new ZipCodeRange(94226, 94399));
        PowerMockito.when(shippingServiceSpy.getListZipCodeRanges()).thenReturn(returnedListOfZipCode);

        List<ZipCodeRange> listOfConsolidateShippingZipCodes = Arrays.asList(new ZipCodeRange(94133, 94133),
                new ZipCodeRange(94200, 94399));
        PowerMockito.when(shippingServiceSpy.getConsolidatedShippableZipCodeRanges()).thenReturn(listOfConsolidateShippingZipCodes);

        List<ZipCode> expectedToBeDelivered = Arrays.asList(new ZipCode(94133),
                new ZipCode(94200),
                new ZipCode(94226));

        for (int index = 0; index < expectedToBeDelivered.size(); index++) {
            assertThat(shippingService.canBeShippedToZipCode(expectedToBeDelivered.get(index))).as("Can be shipped").isTrue();
        }

        List<ZipCode> expectedDeliveriesToFail= Arrays.asList(new ZipCode(94130),
                new ZipCode(94199),
                new ZipCode(94450));
        for (int index = 0; index < expectedToBeDelivered.size(); index++) {
            assertThat(shippingService.canBeShippedToZipCode(expectedDeliveriesToFail.get(index))).as("Can not be shipped").isFalse();
        }
    }
}