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

public class YamlTests {

    String sampleCsvFile = "mock_data/yaml_test_data/CSV-MOCK2.csv";
    String sampleJsonFile = "mock_data/yaml_test_data/MOCK_DATA2.json";
    String sampleYamlFile = "mock_data/yaml_test_data/YAML-MOCK2.yaml";
    String sampleXmlFile = "mock_data/yaml_test_data/MOCK-DATA2.xml";

    @Test
    public void shouldConvertSampleYamlFileToCorrespondingJson() throws IOException {
        ConversionType inputConversionType = ConversionType.YAML;
        ConversionType outputConversionType = ConversionType.JSON;
        String output = "CONVERTED_JSON_TO_YAML.yaml";
        Converter converter = new Converter(sampleYamlFile, output, inputConversionType, outputConversionType, true);
        converter.convert();
        File outputFile = new File(output);
        Assertions.assertTrue(FileUtils.contentEquals(new File(sampleJsonFile), outputFile));
        outputFile.deleteOnExit();
    }
    @Test
    public void shouldConvertSampleYamlFileToCorrespondingXml() throws IOException {
        ConversionType inputConversionType = ConversionType.YAML;
        ConversionType outputConversionType = ConversionType.XML;
        String output = "CONVERTED_YAML_TO_XML.xml";
        Converter converter = new Converter(sampleYamlFile, output, inputConversionType, outputConversionType, true);
        converter.convert();
        File outputFile = new File(output);
        String created = Files.readString(Path.of("CONVERTED_YAML_TO_XML.xml"), StandardCharsets.US_ASCII);
        String source = Files.readString(Path.of("mock_data/yaml_test_data/MOCK_DATA2.xml"), StandardCharsets.US_ASCII).trim();
        Assertions.assertTrue(created.equals(source));
        outputFile.deleteOnExit();
    }
}
