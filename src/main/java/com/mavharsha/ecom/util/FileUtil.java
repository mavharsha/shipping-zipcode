package com.mavharsha.ecom.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public class FileUtil {

    private FileUtil(){}

    public static String readFileFromResource(String  fileName) throws IOException {
        URL fileUrl = Resources.getResource(fileName);
        return readFileFromResource(fileUrl);
    }

    public static String readFileFromResource(URL fileUrl) throws IOException {
        String text = Resources.toString(fileUrl, Charsets.UTF_8);
        return text;
    }
}
