package com.agh.kompilatory.ConverterUtils.Yaml;

import com.agh.kompilatory.ConverterUtils.IConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.codehaus.plexus.util.StringUtils;

import java.io.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlConverter implements IConverter {
    private static final YamlConverter INSTANCE = new YamlConverter();

    public static YamlConverter getInstance(){
        return INSTANCE;
    }

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
    public String convertToJson(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        StringBuilder output = new StringBuilder();
        boolean isNextLineRecord = false;
        int level = 0;

        int row = 0;


        Pattern opening = Pattern.compile("(- )([a-zA-Z0-9]+)(:)");
        Pattern elemLine = Pattern.compile("([a-zA-Z0-9]+)(: )");


        output.append("[");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {


                Matcher openingMatcher = opening.matcher(st);
                Matcher elemLineMatcher = elemLine.matcher(st);



                if (openingMatcher.find()) {
                    if(level>=1){
                        output.append("\t\t}");
                        output.append("\n\t},");
                    }
                    output.append("\n\t{"+'\n');
                    output.append("\t\t\""+ st.replaceAll("[^a-zA-Z]", "")+"\":");

                    output.append("{\n");
                    level=level+1;


                }else if (elemLineMatcher.find()) {
                        row=row+1;

                            if(row==4){
                                output.append("\t\t\t\"" + (elemLineMatcher.group(1)) + "\":");
                                output.append("\"" + st.replaceAll("([a-zA-Z]+(: ))", "") + "\"\n");
                                row=0;
                            }else {

                                output.append("\t\t\t\"" + (elemLineMatcher.group(1)) + "\":");
                                output.append("\"" + st.replaceAll("([a-zA-Z]+(: ))", "") + "\",\n");

                            }
                }

            }
        }catch (Exception e){
            output = new StringBuilder();
            System.out.println(e.getMessage());
        }
        output.append('\n'+"}");
        output.append('\n'+"}");
        output.append('\n'+"]");
        return output.toString();
    }
    @Override
    public String convertToXml(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Stack<String> closers = new Stack<>();
        StringBuilder output = new StringBuilder();
        int level = 0;
        int row = 0;
        String replace = "";
        Pattern opening = Pattern.compile("(- )([a-zA-Z0-9]+)(:)");
        Pattern elemLine = Pattern.compile("([a-zA-Z0-9]+)(: )([a-zA-Z0-9]+)");

        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        output.append("\n<root>\n");

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {

                Matcher openingMatcher = opening.matcher(st);
                Matcher elemLineMatcher = elemLine.matcher(st);
                if (openingMatcher.find()) {
                    if (level >= 1) {
                        int zerowy = level - 1;


                        output.append("\t</row-" + zerowy + ">");


                    }
                    replace = st;
                    output.append("\n\t" + "<row-" + level + ">" + '\n');
                    output.append("\t\t<" + st.replaceAll("[^a-zA-Z]", "") + ">\n");


                    level = level + 1;
                }else if (elemLineMatcher.find()) {
                    row=row+1;
                        output.append("\t\t<" + st.replaceAll("(.*)([a-zA-Z0-9]+)(: )([a-zA-Z0-9]+)", elemLineMatcher.group(1)) + ">");
                        output.append((elemLineMatcher.group(3)) );
                        output.append("</" + st.replaceAll("(.*)([a-zA-Z0-9]+)(: )([a-zA-Z0-9]+)", elemLineMatcher.group(1)) + ">\n");
                }

                if (row == 4) {
                    int zerowy = level - 1;
                    System.out.println(row);

                    row = 0;
                    output.append("\t\t" + "</" + replace.replaceAll("[^a-zA-Z]", "") + ">\n");



                }




            }
            int zeroend = level-1;
            output.append("\t</row-"+zeroend+">\n");
            output.append("</root>");
            return output.toString();
        } catch (IOException e) {
            output = new StringBuilder();
            System.out.println(e.getMessage());
        }return output.toString();
    }
}


