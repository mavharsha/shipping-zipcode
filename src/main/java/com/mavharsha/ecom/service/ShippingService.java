package com.mavharsha.ecom.service;

import com.google.common.collect.ImmutableList;
import com.mavharsha.ecom.model.ZipCode;
import com.mavharsha.ecom.model.ZipCodeRange;
import com.mavharsha.ecom.util.FileUtil;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
public class ShippingService implements IShippingService {

    private String fileName;
    private List<ZipCodeRange> listOfZipCodeRanges;
    private List<ZipCodeRange> listOfShippableZipCodes;


    /**
     * Default constructor, and uses default input zipCodeRanges file.
     *
     * @throws IOException
     */
    public ShippingService() {
        this("ListOfShippingZipCodeRanges.json");
    }


    /**
     * @param file name of the file in resources that contains zipCodeRanges
     */
    public ShippingService(String file) {
        this.fileName = file;
        this.listOfZipCodeRanges = new ArrayList<>();
        this.listOfShippableZipCodes = new ArrayList<>();
    }


    /**
     * @return list Of ZipCodeRanges read from the input file
     */
    @Override
    public List<ZipCodeRange> getListZipCodeRanges() throws IOException {
        if (listOfZipCodeRanges.size() == 0) {
            String json = FileUtil.readFileFromResource(this.fileName);
            Moshi moshi = new Moshi.Builder().build();
            Type type = Types.newParameterizedType(List.class, ZipCodeRange.class);
            JsonAdapter<List<ZipCodeRange>> adapter = moshi.adapter(type);
            listOfZipCodeRanges = adapter.fromJson(json);
            Collections.sort(listOfZipCodeRanges);
        }
        return ImmutableList.copyOf(listOfZipCodeRanges);
    }


    @Override
    public List<ZipCodeRange> getConsolidatedShippableZipCodeRanges() throws Exception {

        if (listOfShippableZipCodes.size() == 0) {

            List<ZipCodeRange> listOfZipCodeRanges = getListZipCodeRanges();
            listOfShippableZipCodes = new ArrayList<>();

            if (listOfZipCodeRanges.size() == 0) {
                throw new Exception("Consolidated shipping range list either null or empty.");
            }

            int indexOfShippableZipCode = 0;
            ZipCodeRange currentShippableZipCodeRange, currentZipCodeRange, mergedZipCodeRange;
            listOfShippableZipCodes.add(indexOfShippableZipCode, listOfZipCodeRanges.get(0));

            for (int index = 1; index < listOfZipCodeRanges.size(); index++) {
                currentShippableZipCodeRange = listOfZipCodeRanges.get(indexOfShippableZipCode);
                currentZipCodeRange = listOfZipCodeRanges.get(index);
                if(currentShippableZipCodeRange.isRangeMergeable(currentZipCodeRange)) {
                    mergedZipCodeRange = currentShippableZipCodeRange.mergeRange(currentZipCodeRange);
                    listOfShippableZipCodes.remove(indexOfShippableZipCode);
                    listOfShippableZipCodes.add(mergedZipCodeRange);
                } else {
                    listOfShippableZipCodes.add(currentZipCodeRange);
                    indexOfShippableZipCode++;
                }
            }
        }
        return ImmutableList.copyOf(listOfShippableZipCodes);
    }

    @Override
    public boolean canBeShippedToZipCode(@NonNull ZipCode zipCode) throws Exception {
        List<ZipCodeRange> listOfShippableZipCodeRanges = getConsolidatedShippableZipCodeRanges();
        for (int index = 0; index < listOfShippableZipCodeRanges.size(); index++) {
            if(listOfShippableZipCodeRanges.get(index).isZipCodeInRange(zipCode)) {
                return true;
            }
        }
        return false;
    }
}
