package com.mavharsha.ecom.dao;

import com.mavharsha.ecom.model.ZipCodeRange;
import com.mavharsha.ecom.util.FileUtil;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileZipCodeRangeDAO implements IZipCodeRangeDAO {

    private String fileName;
    private List<ZipCodeRange> listOfZipCodeRanges;

    public FileZipCodeRangeDAO() {
        this("ListOfShippingZipCodeRanges.json");
    }

    public FileZipCodeRangeDAO(String fileName) {
        this.fileName = fileName;
        this.listOfZipCodeRanges = new ArrayList<>();
    }

    /**
     * @return list Of ZipCodeRanges read from the input file
     * @throws IOException
     */
    @Override
    public List<ZipCodeRange> getListOfZipCodeRanges() throws IOException {
        if (listOfZipCodeRanges.size() == 0) {
            String json = FileUtil.readFileFromResource(this.fileName);
            Moshi moshi = new Moshi.Builder().build();
            Type type = Types.newParameterizedType(List.class, ZipCodeRange.class);
            JsonAdapter<List<ZipCodeRange>> adapter = moshi.adapter(type);
            listOfZipCodeRanges = adapter.fromJson(json);
        }
        return Collections.unmodifiableList(listOfZipCodeRanges);
    }
}
