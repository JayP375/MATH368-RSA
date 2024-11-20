import java.util.Random;
import java.math.BigInteger;

public class Prime{


    public static int getNumber(){ //returns random number 0-9
        Random rand = new Random();
        int number = rand.nextInt(10); 
        return number;
    }

    public static BigInteger getBigNumber(){ //BigInt is needed since long is not big enough
        
        StringBuilder numString = new StringBuilder(); //StringBuilder is needed to work with BigInt

        while (true) {
            int firstNumber = getNumber();
            if (firstNumber != 0) {
                numString.append(firstNumber); //make sure number does not start with 0
                break;
            }
        }

        while(numString.length() < 100){
            numString.append(getNumber());//append random numbers 0-9 until 100 digits are reached
        }

        System.out.println("Generated 100 digit number is: " + numString.toString()); 
        return new BigInteger(numString.toString());

    }

    public static BigInteger getPrime(BigInteger n){
        BigInteger number = n;
        if(primalityTest(number)){ //if the random number generated is a prime, return that number
            return number;
        } else{
            while(!primalityTest(number)){ //otherwise increment and test repeatedly until prime is found
                number = number.add(BigInteger.ONE); 
            }
        }
        return number;
    }

    public static boolean primalityTest(BigInteger n){

        return n.isProbablePrime(100);//built in Miller-Rabin Test from BigInt library
        //Need to develop this method

    }

    public static void main(String[] args){
        System.out.println("The prime number is: " + getPrime(getBigNumber()).toString());
    }
}