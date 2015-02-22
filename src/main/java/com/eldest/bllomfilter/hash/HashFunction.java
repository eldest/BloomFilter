package com.eldest.bllomfilter.hash;

public interface HashFunction {

    String getName();

    int hash(String value);

    int index(String value, int size);
}
