package com.actividadaprendizaje.bookshelter.exception;

/**
 * Excepción para controlar un fallo a la hora de modificar un usuario
 */
public class UserModificationException extends Exception {

    public UserModificationException() {
        super();
    }

    public UserModificationException(String message) {
        super(message);
    }

}
