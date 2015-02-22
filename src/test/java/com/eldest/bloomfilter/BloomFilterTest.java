package com.eldest.bloomfilter;

import com.eldest.bllomfilter.BloomFilter;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class BloomFilterTest {
    private final static Logger LOG = LoggerFactory.getLogger(BloomFilterTest.class);

    private static final Charset ENCODING = Charset.forName("cp1251");

    private static Path wordListPath;


    //--------------------------------- support ---------------------------------

    private Path getResource(String fileName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(fileName);
        return Paths.get(url.toURI());                  //todo: should I care about it?
    }

    private void check(BloomFilter bloomFilter, String valueToCheck, boolean expectedResult) {
        boolean checkResult = bloomFilter.check(valueToCheck);
        assertThat(checkResult, is(expectedResult));

        if (checkResult) {
            LOG.info("value '{}' is probably there.", valueToCheck);
        } else {
            LOG.info("value '{}' is definitely not there.", valueToCheck);
        }
    }

    //--------------------------------- tests ---------------------------------

    @Before
    public void setUp() throws Exception {
        wordListPath = getResource("wordlist.txt");
    }

    @Test
    public void testBloomFilter() throws Exception {
        List<String> strings = Files.lines(wordListPath, ENCODING)
                .collect(Collectors.toList());

        BloomFilter bloomFilter = new BloomFilter.Builder().setFilterData(strings).build();

        LOG.info("List size: {}", strings.size());
        LOG.info("BloomFilter size: {}", bloomFilter.size());
        bloomFilter.getHashFunctions()
                .forEach(hashFunction -> LOG.info("BloomFilter function: {}", hashFunction));

        check(bloomFilter, "777", false);
        check(bloomFilter, "waffs", true);
        check(bloomFilter, "waffsd", false);
        check(bloomFilter, "unvizards", true);
        check(bloomFilter, "way's", true);
        check(bloomFilter, "crullers", true);
    }

    @Test
    public void testGetOptimal() throws Exception {
        int numberOfData = 216_553;

        int numberOfBits = BloomFilter.getOptimalNumberOfBits(numberOfData, 0.01);
        LOG.info("Optimal number of bits: {}", numberOfBits);
        assertThat(numberOfBits, equalTo(2075674));

        int numberOfFunctions = BloomFilter.getOptimalNumberOfFunctions(numberOfBits, numberOfData);
        LOG.info("Optimal number of functions: {}", numberOfFunctions);
        assertThat(numberOfFunctions, equalTo(7));
    }
}
