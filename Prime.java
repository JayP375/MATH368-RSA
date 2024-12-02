import java.util.Random;
import java.math.BigInteger;

public class Prime{


    public static int getNumber(){ //returns random number 0-9
        Random rand = new Random();
        int number = rand.nextInt(10); 
        return number;
    }

    public static BigInteger getBigNumber(){ //BigInt is needed since long is not big enough
        
        StringBuilder numString = new StringBuilder(); //StringBuilder is needed to concatenate a 100 digit string to be used as a BigInt

        while (true) {
            int firstNumber = getNumber();
            if (firstNumber != 0) {
                numString.append(firstNumber); //make sure number does not start with 0
                break;
            }
        }

        while(numString.length() < 99){
            numString.append(getNumber());//append random numbers 0-9 to numbString until 99 digits are reached
        }

        int lastNumber = getNumber();

        if(lastNumber % 2 == 0){//want last number to be odd because prime numbers are always odd
            lastNumber++;
        }

        numString.append(lastNumber);//guarantees last number is odd, meaning entire number is odd


        System.out.println("Generated 100 digit number is: " + numString.toString()); 
        return new BigInteger(numString.toString());

    }

    public static BigInteger getA(){ //same process as getBigNumber, just creates a 99 digit number to use as a base for testing
        
        StringBuilder numString = new StringBuilder(); //StringBuilder is needed to concatenate a 99 digit string to be used as a BigInt

        while (true) {
            int firstNumber = getNumber();
            if (firstNumber != 0) {
                numString.append(firstNumber); //make sure number does not start with 0
                break;
            }
        }

        while(numString.length() < 99){
            numString.append(getNumber());//append random numbers 0-9 until 99 digits are reached
        }

        return new BigInteger(numString.toString());

    }

    public static BigInteger getPrime(BigInteger n){
        BigInteger number = n;
        if(primalityTest(number)){ //if the random number generated is a prime, return that number
            return number;
        } else{
            while(!primalityTest(number)){ //otherwise add 2 and test repeatedly until prime is found
                number = number.add(BigInteger.TWO); 
            }
        }
        System.out.println(number + " is prime!");
        return number;
    }

    public static boolean primalityTest(BigInteger n){

        return millerRabinTest(n);//return t if prime, f otherwise

    }


    public static boolean millerRabinTest(BigInteger n){
        
        BigInteger d = n.subtract(BigInteger.ONE); //need to represent n-1 as d * 2^r

        int r = 0;

        while(d.mod(BigInteger.TWO).equals(BigInteger.ZERO)){

            d = d.divide(BigInteger.TWO); //Keep dividing n-1 by two
            r++; //Increment r. 
            //For Example: 8 -> 4^2 -> 2^3, where the base is d and the exponent is r
        }

        for(int i = 0; i < 20; i++ ){ //test prime number 20 times

            BigInteger a = getA(); //get a random 99 digit a value

            BigInteger test = a.modPow(d,n); //test a^d mod n

            if (test.equals(BigInteger.ONE)){
                continue; //if a^d mod n is 1, then it is probably prime - continue testing with new a
            }

            boolean isComposite = true;
            for(int j = 0; j < r - 1; j++){ //if a^d mod n did not equal 1, the we square it r-1 times, testing each time we sqaure r.
                test = test.modPow(BigInteger.TWO, n);//test = test^2 mod n
                

                if(test.equals(n.subtract(BigInteger.ONE))){
                    isComposite = false; //This test is successful, we retest with a new base a
                    break;
                }


            }


            if(isComposite){ 
 
                return false; //if after 20 interations isComposite is true, then the number is composite
            }
        }
  
        return true; //if after 20 tests isComposite remains false, then the number is prime

    }

    public static void main(String[] args){

        getPrime(getBigNumber()); //delete if neccessary, used for testing
        
    }
}
