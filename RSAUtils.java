import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RSAUtils {

    // Calculate gcd(a, b) using the Euclidean algorithm for BigIntegers
    static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        }
        return gcd(b, a.mod(b));
    }

    // Calculate a^b mod m using modular exponentiation for BigIntegers
    static BigInteger modpow(BigInteger a, BigInteger b, BigInteger m) {
        BigInteger result = BigInteger.ONE;
        while (b.compareTo(BigInteger.ZERO) > 0) {
            if (b.testBit(0)) { // If b is odd
                result = result.multiply(a).mod(m);
            }
            a = a.multiply(a).mod(m);
            b = b.shiftRight(1); // b >>= 1
        }
        return result;
    }

    // Function for choosing an e -> chooses a random number and checks whether it is relatively prime to phi(n)
    static BigInteger generateE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e ;
        do {
            // Generate a random BigInteger less than phi
            e = new BigInteger(phi.bitLength(), rand);
            // Ensure e is greater than 1 and less than phi
        } while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phi) >= 0 || !gcd(e, phi).equals(BigInteger.ONE));
        return e;
    }

    // Function to calculate d using the Extended Euclidean Algorithm
    static BigInteger computeModularInverse(BigInteger e, BigInteger phi) {
        // Use the Extended Euclidean Algorithm to compute gcd(e, phi) and coefficients x, y
        BigInteger[] result = extendedGCD(e, phi);
        
        // The modular inverse d is the second coefficient in the result
        BigInteger d = result[1];
        
        // If d is negative, we adjust it to ensure it is within the valid range [0, phi)
        if (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(phi); // Add phi to make d positive
        }
        return d; // Return the positive modular inverse
    }

    // Extended Euclidean Algorithm to compute gcd(a, b) and coefficients x, y such that a * x + b * y = gcd(a, b)
    static BigInteger[] extendedGCD(BigInteger a, BigInteger b) {
        // Base case: if b is zero, then gcd(a, b) is a, and the coefficients are 1 and 0
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        }
        
        // Recursive call: apply the extended Euclidean algorithm to b and a mod b
        BigInteger[] result = extendedGCD(b, a.mod(b));
        
        // Extract the gcd and coefficients from the recursive result
        BigInteger gcd = result[0];  // The gcd of a and b
        BigInteger x = result[2];    // The coefficient x from the previous call
        BigInteger y = result[1].subtract(a.divide(b).multiply(result[2])); // Update the coefficient y
        
        // Return the gcd and coefficients x, y
        return new BigInteger[]{gcd, x, y};
    }

    // Helper to calculate phi(n) = (p-1)*(q-1), where n is a prime number
    static BigInteger calculatePhi(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    //Helper Functions
    // Encode a String to a BigInteger
    public static BigInteger encodeStringToBigInteger(String input) {
        // Convert the string to bytes using UTF-8 encoding
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);

        // Create a BigInteger from the byte array
        return new BigInteger(1, bytes); // 1 ensures non-negative values
    }

    // Decode a BigInteger to a String
    public static String decodeBigIntegerToString(BigInteger bigInteger) {
        // Get the byte array from the BigInteger
        byte[] bytes = bigInteger.toByteArray();

        // Convert the byte array back to a string using UTF-8 encoding
        // Remove leading zero if present to handle sign bit
        if (bytes[0] == 0) {
            bytes = java.util.Arrays.copyOfRange(bytes, 1, bytes.length);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    // Helper Function to Check if the String of message Fits in n
    public static boolean canFitInN(String input, BigInteger n) {
        // Convert the string to bytes using UTF-8 encoding
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);

        // Create a BigInteger from the byte array
        BigInteger message = new BigInteger(1, bytes);

        // Return true if the message is smaller than n
        return message.compareTo(n) < 0;
    }
}
