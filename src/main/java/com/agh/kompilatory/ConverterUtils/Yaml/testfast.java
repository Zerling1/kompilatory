package com.agh.kompilatory.ConverterUtils.Yaml;
import com.agh.kompilatory.ConverterUtils.IConverter;
import com.agh.kompilatory.ConverterUtils.Json.JsonConverter;
import com.agh.kompilatory.ConverterUtils.Utils;
import com.agh.kompilatory.Exceptions.InvalidFileFormatException;
import org.codehaus.plexus.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testfast implements IConverter {
    private static final testfast INSTANCE = new testfast();

    public static testfast getInstance(){
        return INSTANCE;
    }

    String filePath = "mock_data/ymal_test_data/YAML-MOCK.yaml";


    @Override
    public boolean validateFile(String filePath) {
        return true;
    }

    @Override
    public String convertToCsv(String filePath) throws FileNotFoundException {

        return null;
    }

    @Override
    public String convertToYaml(String filePath) throws FileNotFoundException {
        throw new FileNotFoundException("Converting to same format not allowed");
    }

    @Override
    public String convertToXml(String filePath) throws FileNotFoundException {
        return null;
    }


    public String convertToJson(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        StringBuilder output = new StringBuilder();
        boolean isNextLineRecord = false;
        int level = 0;

        Pattern opening = Pattern.compile("([\\s]+)(\")([a-zA-Z0-9]+)(\": [{\\[])");
        Pattern oneLine = Pattern.compile("([\\s]+)(\")([^\"]+)(\": )([\"]?)([^\"]+)([\"]?)([,]?)");
        Pattern arrayEl = Pattern.compile("([\\s]+)(\")([^\"]+)(\"[,]?)");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                if (st.endsWith(",")) st = StringUtils.chop(st);

                Matcher openingMatcher = opening.matcher(st);
                Matcher oneLineMatcher = oneLine.matcher(st);
                Matcher arrayElMatcher = arrayEl.matcher(st);

                if (openingMatcher.find()) {
                    String replacement = "  ".repeat(level) + "$3:";
                    output.append(openingMatcher.replaceFirst(replacement));
                    output.append('\n');
                } else if (oneLineMatcher.find()){
                    String replacement = "";
                    String value = oneLineMatcher.group(6);
                    if (isNextLineRecord) {
                        if ((value.contains(":") && value.contains(" ")) || (!value.matches("(.*)([a-zA-Z]+)(.*)") && !oneLineMatcher.group(5).isEmpty()))
                            replacement = "  ".repeat(level - 1) + "- $3: \"$6\"";
                        else
                            replacement = "  ".repeat(level - 1) + "- $3: $6";
                    }
                    else {
                        if ((value.contains(":") && value.contains(" ")) || (!value.matches("(.*)([a-zA-Z]+)(.*)") && !oneLineMatcher.group(5).isEmpty()))
                            replacement = "  ".repeat(level) + "$3: \"$6\"";
                        else
                            replacement = "  ".repeat(level) + "$3: $6";
                    }
                    output.append(oneLineMatcher.replaceFirst(replacement));
                    output.append('\n');
                } else if (arrayElMatcher.find()){
                    String replacement = "  ".repeat(level) + "- $3";
                    output.append(arrayElMatcher.replaceFirst(replacement));
                    output.append('\n');
                }

                isNextLineRecord = st.contains("{");
                if (st.contains("{")) level++;
                else if (st.contains("}")) level--;
            }
        }catch (Exception e){
            output = new StringBuilder();
            System.out.println(e.getMessage());
        }
        return output.toString();
    }
}
