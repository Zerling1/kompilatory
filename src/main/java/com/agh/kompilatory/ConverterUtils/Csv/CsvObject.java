package com.agh.kompilatory.ConverterUtils.Csv;

import java.util.ArrayList;
import java.util.List;

public class CsvObject {
    List<String> headers;
    List<List<String>> data;

    public CsvObject(List<String> headers, List<List<String>> data) {
        this.headers = headers;
        this.data = data;
    }

    CsvObject() {
        this.data = new ArrayList<>();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CsvObject{" +
                "headers=" + headers +
                ", data=" + data +
                '}';
    }
}
