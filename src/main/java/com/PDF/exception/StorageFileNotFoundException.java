package com.PDF.exception;

/**
 * Created by martanase on 12/9/2016.
 */
public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {super(message); }

    public StorageFileNotFoundException(String message, Throwable cause) { super(message, cause); }


}
