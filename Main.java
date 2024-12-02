import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BigInteger p = Prime.getPrime();                        // First Prime Number (Part of Private Key)
        BigInteger q = Prime.getPrime();                        // Second Prime Number (Part of Private Key)
        BigInteger n = p.multiply(q);                           // The product of the 2 primes, Let n = pq (Part of Public Key)
        BigInteger phiN = RSAUtils.calculatePhi(p, q);          //Calculate PHI(n) = (p-1)*(q-1)

        //System.out.println("p: " + p);
        //System.out.println("q: " + q);
        //System.out.println("n (p*q): " + n);
        //System.out.println("phi(n): " + phiN);

        BigInteger e = RSAUtils.generateE(phiN);                // A random number where GCD(e,phi(n)) = 1 (Part of Public Key)
        BigInteger d = RSAUtils.computeModularInverse(e, phiN); // Need to solve to be able to decode the message (Part of Private Key)

        System.out.println("e (public exponent): " + e);
        //System.out.println("d (private key): " + d);

        //Initialize the variables
        String initialMessage = null;
        BigInteger encodedMessage;                              // ASCII-encoded message
        BigInteger encryptedMessage;                            // Encrypted using RSA
        BigInteger decryptedMessage;                            // Decrypted back to encoded message
        String decodedMessage;                                  // Converted back to the original message

        // Prompt user for a message in a loop
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                // Prompt user for a message
                System.out.println("\nType your message (Press Ctrl+C to exit):");
                initialMessage = scanner.nextLine();

                // Check if the message exceeds 66 characters
                if (initialMessage.length() > 66) {
                    System.out.println("\nError: Message exceeds 66 characters. Please try again.");
                    continue;
                }

                // Encode the message to a BigInteger and display it
                encodedMessage = RSAUtils.encodeStringToBigInteger(initialMessage);
                System.out.println("\nEncoded Message using UTF-8:\n" + encodedMessage );

                // Encrypt the message with RSA and display it
                encryptedMessage = RSAUtils.modpow(encodedMessage, e, n);
                System.out.println("\nEncrypted Message:\n" + encryptedMessage);

                // Decrypt the message and display it
                decryptedMessage = RSAUtils.modpow(encryptedMessage, d, n);
                System.out.println("\nDecrypted Encoded Message:\n" + decryptedMessage);

                // Decode the message back to a string and display it
                decodedMessage = RSAUtils.decodeBigIntegerToString(decryptedMessage);
                System.out.println("\nDecoded Message:\n" + decodedMessage);
            } catch (Exception exception) {
                System.out.println("Exiting...\n\n");
                break;
            }
        }
    }
}                   