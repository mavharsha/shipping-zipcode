package com.mavharsha.ecom.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import com.mavharsha.ecom.dao.FileZipCodeRangeDAO;
import com.mavharsha.ecom.dao.IZipCodeRangeDAO;
import com.mavharsha.ecom.model.ZipCode;
import com.mavharsha.ecom.model.ZipCodeRange;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ShippingService implements IShippingService {

    private IZipCodeRangeDAO zipCodeRangeDAO;
    private List<ZipCodeRange> listOfZipCodeRanges;
    private List<ZipCodeRange> listOfShippableZipCodes;

    /**
     * Default constructor, and uses {@link FileZipCodeRangeDAO} as default dataSource
     * @throws IOException
     */
    public ShippingService() {
        this(new FileZipCodeRangeDAO());
    }

    /**
     * @param zipCodeRangeDAO dataSource for getting list of ZipCodeRanges
     */
    public ShippingService(IZipCodeRangeDAO zipCodeRangeDAO) {
        this.zipCodeRangeDAO = zipCodeRangeDAO;
        this.listOfZipCodeRanges = new ArrayList<>();
        this.listOfShippableZipCodes = new ArrayList<>();
    }


    /**
     * @return list of zipCodeRanges from the specified data source
     * @throws IOException when data source is incorrect
     */
    @Override
    public List<ZipCodeRange> getListZipCodeRanges() throws IOException {
        if (listOfZipCodeRanges.size() == 0) {
            listOfZipCodeRanges = Ordering.natural().sortedCopy(zipCodeRangeDAO.getListOfZipCodeRanges());
        }
        return ImmutableList.copyOf(listOfZipCodeRanges);
    }

    /**
     * @return list Of ZipCodeRanges which represent a consolidated list of ZipCodeRanges, to which products can be
     * shipped
     * @throws IOException when data source is incorrect
     */
    @Override
    public List<ZipCodeRange> getConsolidatedShippableZipCodeRanges() throws IOException {

        if (listOfShippableZipCodes.size() == 0) {

            List<ZipCodeRange> listOfZipCodeRanges = getListZipCodeRanges();

            if(listOfZipCodeRanges.size() > 0) {
                listOfShippableZipCodes = new ArrayList<>();
                int indexOfShippableZipCode = 0;
                ZipCodeRange currentShippableZipCodeRange, currentZipCodeRange, mergedZipCodeRange;
                listOfShippableZipCodes.add(indexOfShippableZipCode, listOfZipCodeRanges.get(0));

                for (int index = 1; index < listOfZipCodeRanges.size(); index++) {
                    currentShippableZipCodeRange = listOfZipCodeRanges.get(indexOfShippableZipCode);
                    currentZipCodeRange = listOfZipCodeRanges.get(index);
                    if (currentShippableZipCodeRange.isRangeMergeable(currentZipCodeRange)) {
                        mergedZipCodeRange = currentShippableZipCodeRange.mergeRange(currentZipCodeRange);
                        listOfShippableZipCodes.remove(indexOfShippableZipCode);
                        listOfShippableZipCodes.add(mergedZipCodeRange);
                    } else {
                        listOfShippableZipCodes.add(currentZipCodeRange);
                        indexOfShippableZipCode++;
                    }
                }
            }
        }
        return ImmutableList.copyOf(listOfShippableZipCodes);
    }

    /**
     * @param zipCode A zipCode
     * @return true if zipCode can be shipped, else false
     * @throws Exception
     */
    @Override
    public boolean canBeShippedToZipCode(@NonNull ZipCode zipCode) throws IOException {
        List<ZipCodeRange> listOfShippableZipCodeRanges = getConsolidatedShippableZipCodeRanges();
        for (int index = 0; index < listOfShippableZipCodeRanges.size(); index++) {
            if (listOfShippableZipCodeRanges.get(index).isZipCodeInRange(zipCode)) {
                return true;
            }
        }
        return false;
    }
}
