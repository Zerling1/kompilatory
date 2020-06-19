package com.agh.kompilatory;

import com.agh.kompilatory.ConverterUtils.Converter;
import com.agh.kompilatory.ConverterUtils.Utils;
import com.agh.kompilatory.Exceptions.InvalidFileFormatException;
import com.agh.kompilatory.Providers.ConversionType;
import com.agh.kompilatory.Validators.InputFileValidator;

public class Main {

    public static void main(String[] args) {
        String filePath = "mock_data/yaml_test_data/YAML-MOCK2.yaml";
        String outputFilePath = "mock_data/yaml_test_data/MOCK_DATA2.json";
        ConversionType outputConversionType = ConversionType.JSON;
        if (InputFileValidator.validate(filePath)) {
            try {
                ConversionType inputConversionType = Utils.getInputFileType(filePath);
                Converter converter = new Converter(filePath, outputFilePath, inputConversionType, outputConversionType, true);
                converter.convert();

            }
            catch (InvalidFileFormatException e) {
                System.out.println(e.getMessage() + " " + e.getErrorLine());
            }

        }
    }
}
