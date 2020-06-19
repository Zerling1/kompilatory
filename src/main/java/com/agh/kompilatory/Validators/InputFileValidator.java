package com.agh.kompilatory.Validators;

import java.io.File;

public class InputFileValidator{
    public static boolean validate(String filePath){
        return (checkFileExtension(filePath) && checkIfFileExists(filePath));
    }

    private static boolean checkIfFileExists(String filePath){
        File tmpFile = new File(filePath);
        return tmpFile.exists();
    }

    private static boolean checkFileExtension(String filePath){
        return filePath.toLowerCase().endsWith(".json")
                || filePath.toLowerCase().endsWith(".xml")
                || filePath.toLowerCase().endsWith("yaml")
                || filePath.toLowerCase().endsWith(".csv");
    }
}
