import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the RSA Encryption System (Alice & Bob Communication)!\n");

        boolean firstIteration = true; // Track if it's the first iteration

        while (true) {
            if (!firstIteration) {
                // Ask the user if they want to send another message
                System.out.print("\nDo you want to send another message? (yes/no):\n>");
                String response = scanner.nextLine().trim().toLowerCase();

                if (!response.equals("yes")) {
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                }
            }

            try {
                System.out.println("---------------------------------------------------");
                System.out.println("--- Step 1: Key Generation by Alice (Sender) ---");

                // Alice generates RSA keys
                BigInteger p = Prime.getPrime();                          // Alice's first prime number (Part of Private Key)
                BigInteger q = Prime.getPrime();                          // Alice's second prime number (Part of Private Key)
                BigInteger n = p.multiply(q);                             // The product of the 2 primes, Let n = pq (Part of Public Key)
                BigInteger phiN = RSAUtils.calculatePhi(p, q);            // Euler's totient function -> Calculate PHI(n) = (p-1)*(q-1)
                BigInteger e = RSAUtils.generateE(phiN);                  // Public exponent -> A random number where GCD(e,phi(n)) = 1 (Part of Public Key)
                BigInteger d = RSAUtils.computeModularInverse(e, phiN);   // Private key -> Compute to be able to decode the message (Part of Private Key)

                // Alice shares the public key (n, e) with Bob
                System.out.println("Alice's Public Key: { \nn: " + n + ", \ne: " + e + " }");
                System.out.println("Alice's Private Key (kept secret): { \np: " + p + ", \nq: " + q + ", \nd: " + d + " }");

                // Bob prepares to encrypt message
                System.out.println("\n--- Step 2: Bob Receives and Encrypts the Message ---");
                System.out.print("Bob, enter the encrypted message (Type 'exit' to quit):\n>");
                String initialMessage = scanner.nextLine();

                // Exit condition
                if (initialMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the program. Goodbye!\n\n");
                    break;
                }

                // Check if the message fits within the limit imposed by n
                if (!RSAUtils.canFitInN(initialMessage, n)) {
                    System.out.println("Error: Message too large to fit within the RSA key size. Please try again.");
                    continue;
                }

                // Encode Bob's message into a BigInteger
                BigInteger encodedMessage = RSAUtils.encodeStringToBigInteger(initialMessage);
                System.out.println("Bob Encodes Message Using UTF-8:\n" + encodedMessage);

                // Encrypt Bob's message using Alice's public key
                BigInteger encryptedMessage = RSAUtils.modpow(encodedMessage, e, n);
                System.out.println("Bob Encrypts Message:\n" + encryptedMessage);

                // Alice receives the encrypted message
                System.out.println("\n--- Step 3: Alice Receives and Decrypts the Message ---");
                System.out.println("Alice Receives Encrypted Message:\n" + encryptedMessage);

                // Alice decrypts the message using Alice's private key
                BigInteger decryptedMessage = RSAUtils.modpow(encryptedMessage, d, n);
                System.out.println("Alice Decrypts Message:\n" + decryptedMessage);

                // Decode the decrypted message back into a string
                String decodedMessage = RSAUtils.decodeBigIntegerToString(decryptedMessage);
                System.out.println("Alice Decodes Message (Original String):\n" + decodedMessage);

                firstIteration = false;
            } catch (Exception exception) {
                System.out.println("Exiting...\n\n");
                break;
            }
        }

        scanner.close();
    }
}