package com.eldest.bllomfilter.hash;

public interface HashFunction {

    long hash(String value);

    int index(String value, int size);
}
