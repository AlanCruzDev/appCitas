package com.app.citas.excepciones;

public class NoExisteException extends RuntimeException{
    
    public NoExisteException(String message) {
        super(message);
    }
}
