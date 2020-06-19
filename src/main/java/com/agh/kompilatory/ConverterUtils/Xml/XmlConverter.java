package com.agh.kompilatory.ConverterUtils.Xml;


import com.agh.kompilatory.ConverterUtils.IConverter;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.util.List;
import java.io.FileNotFoundException;


public class XmlConverter implements IConverter {

    private static final XmlConverter INSTANCE = new XmlConverter();

    private XmlConverter(){}

    public static XmlConverter getInstance(){
        return INSTANCE;
    }

    @Override
    public boolean validateFile(String filePath) {
        return true;
    }

    @Override
    public String convertToXml(String filePath) throws FileNotFoundException {
        throw new FileNotFoundException("Converting to same format not allowed");
    }
    @Override
    public String convertToYaml(String filePath) {
        return null;
    }
    @Override
    public String convertToCsv(String filePath) {
        return null;
    }
    @Override
    public String convertToJson(String filePath) {
        ArrayList<String> content = new ArrayList<String>();
        ArrayList<String> marks = new ArrayList<String>();
        StringBuilder output = new StringBuilder();
        File file = new File(filePath);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                if(line.contains("/")){
                    System.out.println("kappa");
                }
                else if(line.contains("<")){
                    marks.add(line.replaceAll("\\P{L}+", ""));

                }else {
                    content.add(line);
                    output.append(line);
                    output.append("\n");
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

}
