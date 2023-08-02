package com.mcu.examportal;
import java.security.SecureRandom;

public class KeyGenerator {

    public static void main(String[] args) {
        // Number of bytes for a 256-bit key
        int numBytes = 32;

        // Generate the key bytes
        byte[] keyBytes = new byte[numBytes];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        // Convert the key bytes to hex
        String keyHex = bytesToHex(keyBytes);

        // Print the 256-bit hex encryption key
        System.out.println("256-bit Hex Encryption Key: " + keyHex);
    }

    // Convert a byte array to a hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
