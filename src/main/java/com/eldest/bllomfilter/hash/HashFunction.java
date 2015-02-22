package com.eldest.bllomfilter.hash;

public interface HashFunction {

    int hash(String value);

    int index(String value, int size);
}
