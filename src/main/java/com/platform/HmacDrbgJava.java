package com.platform;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class HmacDrbgJava {

    private final byte[] seed;
    private final AtomicInteger counter;

    public HmacDrbgJava(byte[] seed) {
        this.seed = seed;
        this.counter = new AtomicInteger(0);
    }

    public byte[] generateRandomBytes(int size) {
        byte[] randomBytes = new byte[size];
        int index = 0;
        while (index < size) {
            byte[] data = createData(counter.getAndIncrement());
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] chunk = md.digest(data);
                System.arraycopy(chunk, 0, randomBytes, index, Math.min(32, size - index));
                index += 32;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return randomBytes;
    }

    private byte[] createData(int counterValue) {
        byte[] counterBytes = String.valueOf(counterValue).getBytes();
        return concatArrays(counterBytes, seed);
    }

    private byte[] concatArrays(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }
}
