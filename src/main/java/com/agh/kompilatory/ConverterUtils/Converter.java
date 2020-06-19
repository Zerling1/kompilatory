package com.agh.kompilatory.ConverterUtils;

import com.agh.kompilatory.ConverterUtils.Csv.CsvConverter;
import com.agh.kompilatory.ConverterUtils.Json.JsonConverter;
import com.agh.kompilatory.ConverterUtils.Xml.XmlConverter;
import com.agh.kompilatory.ConverterUtils.Yaml.YamlConverter;
import com.agh.kompilatory.Exceptions.InvalidFileFormatException;
import com.agh.kompilatory.Providers.ConversionType;


public class Converter {
    private IConverter converter;
    private final ConversionType outputType;
    private final String inputPath;
    private final String outputPath;
    private boolean csvHasHeaders = false;

    public Converter(String inputPath, String outputPath, ConversionType inputType, ConversionType outputType, boolean csvHasHeaders) {
        this.csvHasHeaders = csvHasHeaders;
        setConverter(inputType);
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.outputType = outputType;
    }

    public Converter(String inputPath, String outputPath, ConversionType inputType, ConversionType outputType) {
        setConverter(inputType);
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.outputType = outputType;
    }

    public void convert() {
        String outputFileContent = null;

        try {
            switch (outputType) {
                case JSON:
                    outputFileContent = converter.convertToJson(inputPath);
                    break;
                case XML:
                    outputFileContent = converter.convertToXml(inputPath);
                    break;
                case CSV:
                    outputFileContent = converter.convertToCsv(inputPath);
                    break;
                case YAML:
                    outputFileContent = converter.convertToYaml(inputPath);
                    break;
            }
            Utils.saveFile(outputFileContent, outputPath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateInputFile(){
        try {
            return this.converter.validateFile(this.inputPath);
        } catch (InvalidFileFormatException e) {
            return false;
        }
    }

    private void setConverter(ConversionType inputType) {

        switch (inputType) {
            case JSON:
                this.converter = JsonConverter.getInstance();
                break;
            case XML:
                this.converter = XmlConverter.getInstance();
                break;
            case CSV:
                this.converter = CsvConverter.getInstance();
                ((CsvConverter) this.converter).setHasHeaders(this.isCsvHasHeaders());
            case YAML:
                this.converter = YamlConverter.getInstance();
                break;

        }
    }

    private boolean isCsvHasHeaders() {
        return csvHasHeaders;
    }
}
