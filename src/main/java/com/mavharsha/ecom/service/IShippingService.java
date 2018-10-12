package com.mavharsha.ecom.service;

import com.mavharsha.ecom.model.ZipCode;
import com.mavharsha.ecom.model.ZipCodeRange;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;

public interface IShippingService {
    List<ZipCodeRange> getListZipCodeRanges() throws IOException;

    List<ZipCodeRange> getConsolidatedShippableZipCodeRanges() throws IOException;

    boolean canBeShippedToZipCode(@NonNull ZipCode zipCode) throws IOException;
}
