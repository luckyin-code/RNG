package com.platform;

import java.io.FileOutputStream;
import java.security.SecureRandom;

public class RandomBitGeneratorJava {

    public static void main(String[] args) throws Exception {
        int totalBits = 300000000;
        int totalBytes = (totalBits + 7) / 8;

        String filePath = "./secureRandomBits.bin";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            byte[] seed = generateSecureSeed();
            System.out.println("Generated secure seed: " + bytesToHex(seed));
            HmacDrbgJava rng = new HmacDrbgJava(seed);
            int bytesWritten = 0;
            int chunkSize = 1024 * 1024; // 1MB chunk size
            while (bytesWritten < totalBytes) {
                int bytesToWrite = Math.min(chunkSize, totalBytes - bytesWritten);
                byte[] randomData = rng.generateRandomBytes(bytesToWrite);
                fos.write(randomData);
                bytesWritten += bytesToWrite;
            }
        }
        System.out.println("Random bitstream generation complete.");
    }

    private static byte[] generateSecureSeed() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secureSeed = new byte[64];
        secureRandom.nextBytes(secureSeed);
        byte[] timeEntropy = Long.valueOf(System.nanoTime()).toString().getBytes();
        return concatArrays(secureSeed, timeEntropy);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static byte[] concatArrays(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

}
