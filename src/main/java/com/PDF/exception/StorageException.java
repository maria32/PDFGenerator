package com.PDF.exception;

/**
 * Created by martanase on 12/9/2016.
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {super(message); }

    public StorageException(String message, Throwable cause) { super(message, cause); }
}
