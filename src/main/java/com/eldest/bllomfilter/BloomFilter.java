package com.eldest.bllomfilter;

import com.eldest.bllomfilter.hash.HashFunction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eldest.bllomfilter.hash.Hashes.Google.*;

/**
 * @see <a href="http://stackoverflow.com/questions/658439/how-many-hash-functions-does-my-bloom-filter-need">
 * how-many-hash-functions-does-my-bloom-filter-need</a>
 */
public class BloomFilter {

    private final Set<HashFunction> hashFunctions;
    private final boolean[] bitArray;

    private BloomFilter(boolean[] bitArray, Set<HashFunction> hashFunctions) {
        this.bitArray = bitArray;
        this.hashFunctions = hashFunctions;
    }

//    public BloomFilter(List<String> filterData, double falsePositiveRate, Set<HashFunction> hashFunctions) {
//        this(getOptimalNumberOfBits(filterData.size(), falsePositiveRate), hashFunctions);
//    }

    //--------------------------------- Builder ---------------------------------

    public static class Builder {

        private Set<HashFunction> hashFunctions = ImmutableSet.of(
                ADLER32, CRC32, MD5, MURMUR3_32, MURMUR3_128, SHA256, SHA512, SIPHASH24, SIP_HASH_24);

        private boolean[] bitArray;
        private double falsePositiveRate = 0.01;
        private List<String> filterData = ImmutableList.of();

        //--------------------------------- f ---------------------------------

        public Builder setFalsePositiveRate(double falsePositiveRate) {
            this.falsePositiveRate = falsePositiveRate;
            return this;
        }

        public Builder setHashFunctions(Set<HashFunction> hashFunctions) {
            this.hashFunctions = hashFunctions;
            return this;
        }

        public Builder setFilterSize(int filterSize) {
            this.bitArray = new boolean[filterSize];
            return this;
        }

        public Builder setFilterData(List<String> filterData) {
            this.filterData = filterData;
            return this;
        }

        public BloomFilter build() {
            if (!filterData.isEmpty()) {
                int dataSize = filterData.size();

                int numberOfBits = getOptimalNumberOfBits(dataSize, falsePositiveRate);
                this.bitArray = new boolean[numberOfBits];

                int numberOfFunctions = getOptimalNumberOfFunctions(numberOfBits, dataSize);
                hashFunctions = hashFunctions.stream()
                        .limit(numberOfFunctions)
                        .collect(Collectors.toSet());
            }

            BloomFilter bloomFilter = new BloomFilter(bitArray, hashFunctions);
            bloomFilter.addData(filterData);

            return bloomFilter;
        }
    }

    //--------------------------------- f ---------------------------------

    public void addData(String value) {
        for (HashFunction hashFunction : hashFunctions) {
            int index = hashFunction.index(value, bitArray.length);
            bitArray[index] = true;
        }
    }

    public void addData(List<String> valueList) {
        for (String value : valueList) {
            addData(value);
        }
    }

    public boolean check(String value) {
        for (HashFunction hashFunction : hashFunctions) {
            int index = hashFunction.index(value, bitArray.length);
            if (!bitArray[index]) {
                return false;
            }
        }

        return true;
    }

    public Set<HashFunction> getHashFunctions() {
        return hashFunctions;
    }

    public int size() {
        return bitArray.length;
    }

    //--------------------------------- static ---------------------------------

    public static int getOptimalNumberOfBits(int numberOfItems, double falsePositiveRate) {
        return (int) Math.ceil(-numberOfItems * Math.log(falsePositiveRate) / Math.pow(Math.log(2d), 2));
    }

    public static int getOptimalNumberOfFunctions(double numberOfBits, int numberOfItems) {
        return (int) Math.ceil(numberOfBits / numberOfItems * Math.log(2));
    }

}
