package com.eldest.bllomfilter.hash;

import com.eldest.bllomfilter.hash.implementation.*;
import com.google.common.base.MoreObjects;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/** Contains List of Hash Functions to use in BloomFilter.
 * @see <a href="https://code.google.com/p/guava-libraries/wiki/HashingExplained">guava HashingExplained</a>
 * @see <a href="http://www.burtleburtle.net/bob/hash/doobs.html">Hash Functions</a>
 */
public class Hashes {

    private static final Charset ENCODING = Charset.forName("cp1251");


    public static final HashFunction MURMUR = new HashFunctionImpl("MURMUR", Hashes::murmur);

    public static final HashFunction FNV1A32 = new HashFunctionImpl("FNV1a32", value -> fnv(value, FNV1a32::new));
    public static final HashFunction FNV1A64 = new HashFunctionImpl("FNV1a64", value -> fnv(value, FNV1a64::new));
    public static final HashFunction FNV_132 = new HashFunctionImpl("FNV132", value -> fnv(value, FNV132::new));
    public static final HashFunction FNV_164 = new HashFunctionImpl("FNV164", value -> fnv(value, FNV164::new));

    public static final HashFunction MD5 = new HashFunctionImpl("MD5", value -> secured(value, "MD5"));
    public static final HashFunction SHA_1 = new HashFunctionImpl("SHA-1", value -> secured(value, "SHA-1"));
    public static final HashFunction SHA_256 = new HashFunctionImpl("SHA-256", value -> secured(value, "SHA-256"));

    private Hashes() { /* closed */ }

    //--------------------------------- murmur ---------------------------------

    private static final int SEED = 1;

    private static int murmur(String value) {
        return MurmurHash.hash(value.getBytes(), SEED);
    }

    //--------------------------------- fnv ---------------------------------

    private static int fnv(String value, Supplier<FNV1> supplier) {
        FNV1 fnv = supplier.get();
        fnv.init(value);
        return (int) fnv.getHash();
    }

    //--------------------------------- secured ---------------------------------

    private static int secured(String value, String algorithm) {
        try {
            byte[] bytes = value.getBytes(ENCODING);
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] digest = messageDigest.digest(bytes);
            return HashCode.fromBytes(digest).hashCode();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //--------------------------------- HashFunction ---------------------------------

    public static class HashFunctionImpl implements HashFunction {
        private String name;
        private Function<String, Integer> function;

        HashFunctionImpl(String name, Function<String, Integer> function) {
            this.name = name;
            this.function = function;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public int hash(String value) {
            return function.apply(value);
        }

        @Override
        public int index(String value, int size) {
            return Math.abs(hash(value) % size);
        }

        @Override
        public int hashCode() {
            return Objects.hash(function);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final HashFunctionImpl other = (HashFunctionImpl) obj;
            return Objects.equals(this.function, other.function);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
//                    .add("function", function)
                    .toString();
        }
    }

    //--------------------------------- Google ---------------------------------

    public static class Google {

        public static final HashFunction MURMUR3_32 = new HashFunctionImpl(
                "Google.MURMUR3_32",
                value -> getHashCode(Hashing.murmur3_32(), value));

        public static final HashFunction MURMUR3_128 = new HashFunctionImpl(
                "Google.MURMUR3_128",
                value -> getHashCode(Hashing.murmur3_128(), value));

        public static final HashFunction SIPHASH24 = new HashFunctionImpl(
                "Google.SIPHASH24",
                value -> getHashCode(Hashing.sipHash24(), value));

        public static final HashFunction ADLER32 = new HashFunctionImpl(
                "Google.ADLER32",
                value -> getHashCode(Hashing.adler32(), value));

        public static final HashFunction CRC32 = new HashFunctionImpl(
                "Google.CRC32",
                value -> getHashCode(Hashing.crc32(), value));

        public static final HashFunction MD5 = new HashFunctionImpl(
                "Google.MD5",
                value -> getHashCode(Hashing.md5(), value));

        public static final HashFunction SHA256 = new HashFunctionImpl(
                "Google.SHA256",
                value -> getHashCode(Hashing.sha256(), value));

        public static final HashFunction SHA512 = new HashFunctionImpl(
                "Google.SHA512",
                value -> getHashCode(Hashing.sha512(), value));

        public static final HashFunction SIP_HASH_24 = new HashFunctionImpl(
                "Google.SIP_HASH_24",
                value -> getHashCode(Hashing.sipHash24(), value));

        private Google() { /* closed */ }

        //--------------------------------- f ---------------------------------

        private static int getHashCode(com.google.common.hash.HashFunction hashFunction, String value) {
            HashCode hashCode = hashFunction.newHasher()
                    .putString(value, ENCODING)
                    .hash();

            return hashCode.hashCode();
        }

    }


}
