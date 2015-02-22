package com.eldest.bloomfilter.hash;

import com.eldest.bllomfilter.hash.HashFunction;
import com.eldest.bllomfilter.hash.Hashes;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class HashTest {
    private final static Logger LOG = LoggerFactory.getLogger(HashTest.class);

    @Rule
    public TestName name = new TestName();

    private static final String[] INPUT = {"Anton", "12345", "TEST", "something", "123", "777", "012"};
    private static final int FILTER_SIZE = 15;

    //--------------------------------- support ---------------------------------

    private static void testHash(final HashFunction hashFunction) {
        for (String item : Arrays.asList(INPUT)) {
            long hash = hashFunction.hash(item);
            int index = hashFunction.index(item, FILTER_SIZE);

            LOG.info("item='{}', hash='{}', index='{}'", item, hash, index);
        }
    }

    //--------------------------------- tests ---------------------------------


    @Before
    public void setUp() throws Exception {
        LOG.info("------------------------------------------------------------------//{}", name.getMethodName());
    }

    @Test
    public void testMurmur() throws Exception {
        testHash(Hashes.MURMUR);
    }

    @Test
    public void testFNV1a32() throws Exception {
        testHash(Hashes.FNV1A32);
    }

    @Test
    public void testFNV1a64() throws Exception {
        testHash(Hashes.FNV1A64);
    }

    @Test
    public void testFNV132() throws Exception {
        testHash(Hashes.FNV_132);
    }

    @Test
    public void testFNV164() throws Exception {
        testHash(Hashes.FNV_164);
    }

    @Test
    public void testMD5() throws Exception {
        testHash(Hashes.MD5);
    }

    @Test
    public void testSHA_1() throws Exception {
        testHash(Hashes.SHA_1);
    }

    @Test
    public void testSHA_256() throws Exception {
        testHash(Hashes.SHA_256);
    }

    @Test
    public void testGoogleMURMUR3_32() throws Exception {
        testHash(Hashes.Google.MURMUR3_32);
    }

    @Test
    public void testGoogleADLER32() throws Exception {
        testHash(Hashes.Google.ADLER32);
    }

    @Test
    public void testGoogleCRC32() throws Exception {
        testHash(Hashes.Google.CRC32);
    }

    @Test
    public void testGoogleMD5() throws Exception {
        testHash(Hashes.Google.MD5);
    }

    @Test
    public void testGoogleSHA256() throws Exception {
        testHash(Hashes.Google.SHA256);
    }

    @Test
    public void testGoogleSIPHASH24() throws Exception {
        testHash(Hashes.Google.SIPHASH24);
    }
}
