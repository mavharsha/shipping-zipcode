package com.mavharsha.ecom.dao;

import com.mavharsha.ecom.model.ZipCodeRange;

import java.io.IOException;
import java.util.List;

public interface IZipCodeRangeDAO {
    public List<ZipCodeRange> getListOfZipCodeRanges() throws IOException;
}
