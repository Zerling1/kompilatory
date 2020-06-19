package com.agh.kompilatory.Exceptions;

public class InvalidFileFormatException extends Exception {
    private int errorLine;

    public InvalidFileFormatException(String message, int errorLine){
        super(message);
        this.errorLine = errorLine;
    }

    public InvalidFileFormatException(String message){
        super(message);
    }

    public int getErrorLine() {
        return errorLine;
    }
}
