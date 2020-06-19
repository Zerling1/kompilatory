package com.agh.kompilatory.ConverterUtils.Json;

import com.agh.kompilatory.ConverterUtils.IConverter;
import com.agh.kompilatory.ConverterUtils.Utils;
import org.codehaus.plexus.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonConverter implements IConverter {

    private static final JsonConverter INSTANCE = new JsonConverter();

    private JsonConverter(){}

    public static JsonConverter getInstance(){
        return INSTANCE;
    }

    @Override
    public boolean validateFile(String filePath) {
        prepareFile(filePath);
        return true;
    }

    private String prepareFile(String filePath){
        int level = 0;
        StringBuilder result = new StringBuilder();
        try{
            String content = Files.readString(Path.of(filePath));
            char[] arrayContent = content.toCharArray();

            for (int i = 0; i < content.length(); i ++){
                if ((arrayContent[i] == '{' || arrayContent[i] == '[') && arrayContent[i + 1] != '\n'){
                    level++;
                    result.append(arrayContent[i]);
                    result.append("\n");
                    result.append("\t".repeat(level));
                }
                else if ((arrayContent[i] == '}' || arrayContent[i] == ']') && (!Character.isWhitespace(arrayContent[i-1]) && !Character.isWhitespace(arrayContent[i-2]))){
                    level--;
                    result.append("\n");
                    result.append("\t".repeat(level));
                    result.append(arrayContent[i]);
                }
                else if (arrayContent[i] == ',' && arrayContent[i+1] != '\n'){
                    result.append(arrayContent[i]);
                    result.append("\n");
                    result.append("\t".repeat(level));
                }
                else{
                    result.append(arrayContent[i]);
                    if (arrayContent[i] == '{' || arrayContent[i] == '[') level ++;
                    if (arrayContent[i] == '}' || arrayContent[i] == ']') level --;
                }
            }

            Utils.saveFile(result.toString(), filePath);
            return result.toString();


        } catch (Exception e){
            return null;
        }
    }

    @Override
    public String convertToCsv(String filePath) {
        File file = new File(filePath);
        Stack<String> parents = new Stack<>();
        Map<String, List<String>> data = new HashMap<>();
        List<String> headers = new ArrayList<>();
        int arrayElementCounter = 1;
        StringBuilder output = new StringBuilder();


        Pattern opening = Pattern.compile("(\")([a-zA-Z0-9]+)(\": [{\\[])");
        Pattern oneLine = Pattern.compile("([\\s]+)(\")([^\"]+)(\": [\"]?)([^\"]+)([\"]?)([.,]?)");
        Pattern closure = Pattern.compile("([}\\]][,]?)");
        Pattern arrayEl = Pattern.compile("([\\s]+[\"]?)([^\"\\[\\]\\{\\}]+)([\"]?)([,]?)");

        int level = -1;
        int depth = -1;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                if (st.endsWith(",")) st = StringUtils.chop(st);

                Matcher openingMatcher = opening.matcher(st);
                Matcher oneLineMatcher = oneLine.matcher(st);
                Matcher closureMatcher = closure.matcher(st);
                Matcher arrayElMatcher = arrayEl.matcher(st);

                if (st.trim().equals("{")){
                    depth++;
                    if (depth == 0){
                        level++;
                    }
                    if (depth > 0) {
                        parents.push(String.valueOf(arrayElementCounter));
                        arrayElementCounter++;
                    }
                }
                else if (st.trim().equals("},") || st.trim().equals("}")){
                    depth--;
                    if (depth > -1 && !parents.isEmpty()){
                        parents.pop();
                    }
                }

                else if (openingMatcher.find()) {
                    parents.push(openingMatcher.group(2));
                } else if (oneLineMatcher.find()){
                    String key = null;
                    if (parents.size() > 0)
                        key = String.join("/", parents) + "/" + oneLineMatcher.group(3);
                    else
                        key = oneLineMatcher.group(3);

                    String input = oneLineMatcher.group(5);
                    inputData(data, headers, level, key, input);
                } else if (closureMatcher.find()){
                    if (!parents.empty()) {
                        parents.pop();
                        arrayElementCounter = 1;
                    }
                } else if (arrayElMatcher.find()){
                    String key = String.join("/", parents) + "/" + arrayElementCounter;

                    String input = arrayElMatcher.group(2);
                    inputData(data, headers, level, key, input);
                    arrayElementCounter++;
                }
            }

            output.append(String.join(",", headers));
            output.append("\n");

            for (List<String> dataList : data.values()){
                if (dataList.size() < level + 1){
                    for (int i = dataList.size() - 1; i < level; i++)
                        dataList.add("");
                }
            }

            for (int i = 0; i <= level; i++){
                for (int j = 0; j < headers.size(); j++){
                    String header = headers.get(j);
                    if (data.get(header) != null && data.get(header).get(i) != null){
                        output.append(data.get(header).get(i));
                    }
                    if (j != headers.size() - 1){
                        output.append(",");
                    }
                }
                output.append("\n");
            }

        }catch (Exception e){
            output = new StringBuilder();
            System.out.println(e.getMessage());
        }
        return output.toString();
    }

    private void inputData(Map<String, List<String>> data, List<String> headers, int level, String key, String input) {
        if (input.trim().equals("null")) return;

        if (input.contains(","))
            input = "\"" + input + "\"";
        if (!input.contains("\"") && input.contains(","))
            input = input.replace(",", "");

        if (!headers.contains(key))
            headers.add(key);
        if (data.containsKey(key)){
            for (int i = data.get(key).size(); i < level; i ++){
                data.get(key).add("");
            }
            data.get(key).add(level, input);
        } else {
            List<String> initialList = new ArrayList<>();
            for (int i = 0; i < level; i ++){
                initialList.add("");
            }
            initialList.add(level, input);
            data.put(key, initialList);
        }
    }

    @Override
    public String convertToYaml(String filePath) {

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

    @Override
    public String convertToXml(String filePath) {

        File file = new File(filePath);
        Stack<String> closers = new Stack<>();
        StringBuilder output = new StringBuilder();
        int level = 0;

        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        output.append("\n<root>\n");

        Pattern opening = Pattern.compile("(\")([a-zA-Z0-9]+)(\": [{\\[])");
        Pattern oneLine = Pattern.compile("([\\s]+)(\")([^\"]+)(\": )([\"]?)([^\"]+)([\"]?)([,]?)");
        Pattern closure = Pattern.compile("([}\\]][,]?)");
        Pattern arrayEl = Pattern.compile("(\")([^\"]+)(\"[,]?)");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                if (st.endsWith(",")) st = StringUtils.chop(st);

                Matcher openingMatcher = opening.matcher(st);
                Matcher oneLineMatcher = oneLine.matcher(st);
                Matcher closureMatcher = closure.matcher(st);
                Matcher arrayElMatcher = arrayEl.matcher(st);

                if (st.matches("([\\s]+)\\{")){
                    level++;
                    output.append("  ".repeat(level) + "<item>\n");
                    closers.push("item");
                }
                else if (openingMatcher.find()) {
                    output.append(openingMatcher.replaceFirst("<$2>"));
                    closers.push(openingMatcher.group(2));
                    output.append('\n');
                    level++;
                } else if (oneLineMatcher.find()){
                    output.append(oneLineMatcher.replaceFirst("$1<$3>$6</$3>"));
                    output.append('\n');
                } else if (closureMatcher.find()){
                    if (!closers.empty()) {
                        output.append(closureMatcher.replaceFirst("</" + closers.pop() + ">"));
                        output.append('\n');
                    }
                    level--;
                } else if (arrayElMatcher.find()){
                    output.append(arrayElMatcher.replaceFirst("<item>$2</item>"));
                    output.append('\n');
                }
            }
        }catch (Exception e){
            output = new StringBuilder();
            System.out.println(e.getMessage());
        }
        output.append("</root>");
        return output.toString();
    }

    @Override
    public String convertToJson(String filePath) {
        return null;
    }
}
