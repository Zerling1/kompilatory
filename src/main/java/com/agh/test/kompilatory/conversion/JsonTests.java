package com.agh.test.kompilatory.conversion;

import com.agh.kompilatory.ConverterUtils.Converter;
import com.agh.kompilatory.Providers.ConversionType;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


class JsonTests {
    String sampleCsvFile = "mock_data/json_test_data/CSV-MOCK.csv";
    String sampleJsonFile = "mock_data/json_test_data/JSON-MOCK.json";
    String sampleYamlFile = "mock_data/json_test_data/YAML-MOCK.yaml";
    String sampleXmlFile = "mock_data/json_test_data/XML-MOCK.xml";

    @Test
    public void shouldConvertSampleJsonFileToCorrespondingCsv() throws IOException {
        ConversionType inputConversionType = ConversionType.JSON;
        ConversionType outputConversionType = ConversionType.CSV;
        String output = "CONVERTED_JSON_TO_CSV.csv";
        Converter converter = new Converter(sampleJsonFile, output, inputConversionType, outputConversionType, true);
        converter.convert();
        File outputFile = new File(output);
        Assertions.assertTrue(FileUtils.contentEquals(new File(sampleCsvFile), outputFile));
        outputFile.deleteOnExit();
    }

    @Test
    public void shouldConvertSampleJsonFileToCorrespondingYaml() throws IOException {
        ConversionType inputConversionType = ConversionType.JSON;
        ConversionType outputConversionType = ConversionType.YAML;
        String output = "CONVERTED_JSON_TO_YAML.yaml";
        Converter converter = new Converter(sampleJsonFile, output, inputConversionType, outputConversionType, true);
        converter.convert();
        File outputFile = new File(output);
        Assertions.assertTrue(FileUtils.contentEquals(new File(sampleYamlFile), outputFile));
        outputFile.deleteOnExit();
    }

    @Test
    public void shouldConvertSampleJsonFileToCorrespondingXml() throws IOException {
        ConversionType inputConversionType = ConversionType.JSON;
        ConversionType outputConversionType = ConversionType.XML;
        String output = "CONVERTED_JSON_TO_XML.xml";
        Converter converter = new Converter(sampleJsonFile, output, inputConversionType, outputConversionType, true);
        converter.convert();
        File outputFile = new File(output);
        String created = Files.readString(Path.of("CONVERTED_JSON_TO_XML.xml"), StandardCharsets.US_ASCII);
        String source = Files.readString(Path.of("mock_data/json_test_data/XML-MOCK.xml"), StandardCharsets.US_ASCII).trim();
        Assertions.assertTrue(created.equals(source));
        outputFile.deleteOnExit();
    }
}