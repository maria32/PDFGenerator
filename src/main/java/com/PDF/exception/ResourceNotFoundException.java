package com.PDF.exception;

/**
 * Created by martanase on 11/22/2016.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(){
        System.out.println("EXCEPTION: Resource ot found exception.");
    }
}
