package com.PDF.utils;

/**
 * Created by martanase on 3/19/2017.
 */
public enum LockProperties {
    PRINT(1), COPY(2), MODIFY(4);

    private int value;

    LockProperties(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
