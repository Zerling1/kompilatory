package com.agh.kompilatory.ConverterUtils.Csv;

import com.agh.kompilatory.ConverterUtils.IConverter;

import java.io.FileNotFoundException;
import java.util.List;

import static com.agh.kompilatory.ConverterUtils.Csv.CsvConverterUtils.getCsvFile;


/*
    CSV formatting based on https://tools.ietf.org/html/rfc4180
*/

public class CsvConverter implements IConverter {
    private static final CsvConverter INSTANCE = new CsvConverter();
    private boolean hasHeaders = false;


    public static CsvConverter getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean validateFile(String filePath) {
        return true;
    }

    @Override
    public String convertToCsv(String filePath) throws FileNotFoundException {
        throw new FileNotFoundException("Converting to same format not allowed");
    }

    @Override
    public String convertToYaml(String filePath) {
        StringBuilder output = new StringBuilder();
        int index = 1;
        try {
            CsvObject csvObject = getCsvFile(filePath, this.hasHeaders);
            for (List<String> dataCell : csvObject.data) {
                output.append(dataCell.get(0));
                output.append(":");
                output.append("\n\r");
                for (int i = 0; i < dataCell.size(); i++) {
                    if (hasHeaders) {

                        output.append("  ");
                        output.append(csvObject.headers.get(i));
                        output.append(": ");
                        output.append(dataCell.get(i));
                        output.append("\n\r");
                    } else {
                        output.append("  -");
                        output.append(dataCell.get(i));
                        output.append("\n\r");
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    @Override
    public String convertToXml(String filePath) {
        StringBuilder output = new StringBuilder();
        try {
            CsvObject csvObject = getCsvFile(filePath, this.hasHeaders);
            output.append("<?xml version=\"1.0\" ?>");
            output.append("\n\r");
            output.append("<Records>");
            output.append("\n\r");
            for (List<String> dataCell : csvObject.data) {
                output.append("  ");
                output.append("<Record>");
                output.append("\n\r");
                for (int i = 0; i < dataCell.size(); i++) {
                    if (hasHeaders) {
                        output.append("    ");
                        output.append(csvObject.headers.get(i));
                        output.append('=');
                        output.append('"');
                        output.append(dataCell.get(i));
                        output.append('"');
                        output.append("\n\r");
                    } else {
                        output.append("    ");
                        output.append("val_").append(i);
                        output.append('=');
                        output.append('"');
                        output.append(dataCell.get(i));
                        output.append('"');
                        output.append("\n\r");
                    }
                }
                output.append("  ");
                output.append("</Record>");
                output.append("\n\r");
            }
            output.append("</Records>");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    @Override
    public String convertToJson(String filePath) {
        StringBuilder output = new StringBuilder();
        try {
            CsvObject csvObject = getCsvFile(filePath, this.hasHeaders);
            output.append('{');
            output.append("\n\r");
            for (List<String> dataCell : csvObject.data) {
                output.append('"');
                output.append(dataCell.get(0));
                output.append('"');
                output.append(':');
                if (hasHeaders) {
                    output.append('{');
                } else {
                    output.append('[');
                }
                output.append("\n\r");
                for (int i = 0; i < dataCell.size(); i++) {
                    if (hasHeaders) {
                        output.append("  ");
                        output.append('"');
                        output.append(csvObject.headers.get(i));
                        output.append('"');
                        output.append(':');
                        output.append('"');
                        output.append(dataCell.get(i));
                        output.append('"');
                        if (! (i == dataCell.size() - 1)) {
                            output.append(',');
                        }
                        output.append("\n\r");
                    } else {
                        output.append("  ");
                        output.append('"');
                        output.append(dataCell.get(i));
                        output.append('"');
                        if (! (i == dataCell.size() - 1)) {
                            output.append(',');
                        }
                        output.append("\n\r");
                    }
                }
                if (hasHeaders) {
                    output.append('}');
                } else {
                    output.append(']');
                }
                output.append(',');
                output.append("\n\r");
            }
            output.setLength(output.length() - 3);
            output.append("\n\r");
            output.append('}');
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public void setHasHeaders(boolean hasHeaders) {
        this.hasHeaders = hasHeaders;
    }
}
