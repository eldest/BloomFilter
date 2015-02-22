package com.eldest.bllomfilter.serialization;

import java.io.*;

public class Serializations {

    private Serializations() { /* closed */ }

    public static byte[] serialize(Object obj) throws SerializationException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public static Object deserialize(byte[] data) throws SerializationException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            return objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException(e);
        }
    }

}
