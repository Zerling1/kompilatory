package com.agh.kompilatory.ConverterUtils.Csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class CsvConverterUtils {

    static CsvObject getCsvFile(String filePath, boolean hasHeaders) throws IOException {
        File file = new File(filePath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        CsvObject csvObject = new CsvObject();
//        headers
        if (hasHeaders) {
            csvObject.headers = Arrays.asList(bufferedReader.readLine().split(","));
        }
//        data
        String line;
        List<String> row;
        while ((line = bufferedReader.readLine()) != null) {
            row = Arrays.asList(line.split(","));
            csvObject.data.add(row);
        }

        return csvObject;

    }
}
