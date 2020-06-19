package com.agh.kompilatory.ConverterUtils;

import com.agh.kompilatory.Exceptions.InvalidFileFormatException;

import java.io.FileNotFoundException;

public interface IConverter {
    //metodka sprawdzająca, czy plik nie ma niezgodności ze swoim formatem - rzuca wyjątek, w którym można rzucić
    //numer linii, w której wystąpił błąd
    boolean validateFile(String filePath) throws InvalidFileFormatException;

    //metodki zwracające przekonwertowaną treść pliku (może też zwracać to np. w formie byte[], whatever)
    String convertToCsv(String filePath) throws FileNotFoundException;

    String convertToYaml(String filePath) throws FileNotFoundException;

    String convertToXml(String filePath) throws FileNotFoundException;

    String convertToJson(String filePath) throws FileNotFoundException;

}
