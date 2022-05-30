package com.actividadaprendizaje.bookshelter.exception;

public class BookNotFoundException extends Exception{

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException() {
        super();
    }
}
