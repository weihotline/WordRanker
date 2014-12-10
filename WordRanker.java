/* Author: Weihao Li
   Modification Date: 06/17/2014
   File Name: WordRanker.java

   Program Description: WordRanker.java accepts a word as a command line argument
   and prints its rank number, based on where it falls in an alphabetically
   sorted list of all words made up of the same set of letters.
   (e.g. ABC ranked 1, ACB ranked 2, BAC ranked 3, and etc.)

   Basic Concept:
   each letter is assigned to a number according to its alphabetical order within the word.
   (e.g. the word: BDAA: B D A A
                         2 3 1 1)
   A is ranked before B (the first letter of the word), and the number of possible
   arrangements to begin a word with an A is the number of all possible permutations
   with repeated letters.
   Obtain the rank number of the word by calculating the number of all possible permutations
   for the rest of the word until it gets to the last letter
*/

import java.util.*;
import java.math.BigInteger;

public class WordRanker
{
	public static void main (String[] args)
	{
        // this line is for calculating the execution time
        final long startTime = System.currentTimeMillis();

        for (String word: args) {

            System.out.print(word + " = ");

            BigInteger rank = BigInteger.ONE;

            while (word.length() != 0) {  // stop when the word is completely reduced

                String wordClone = word; // make a copy of the word for manipulation
                char[] charArr = wordClone.toCharArray(); // split the word into characters
                Arrays.sort(charArr); // sort the list of chars

                // make a hashMap for each non-repeated letter within the word
                HashMap<Character,Integer> charHash = charArrToHashWithRank(charArr);

                // obtain the first letter of the word
                Character firstCh = new Character(word.charAt(0));
                Integer n = charHash.get(firstCh);

                // for each letter ranked before the firstCh, calculate all possible permutations
                for (int i=1; i<n.intValue(); i++) {

                    char ch = getChar(charHash, i);

                    int foundIndex = wordClone.indexOf(ch);
                    // reconstruct the word without the letter ranked before the firstCh
                    wordClone = wordClone.substring(0,foundIndex) + wordClone.substring(foundIndex+1);

                    char[] subCharArr = wordClone.toCharArray();
                    Arrays.sort(subCharArr);
                    rank = rank.add(permutWithRepeat(subCharArr));

                    wordClone = wordClone + ch; // reconstruct the word
                }

                word = word.substring(1); // remove the firstCh and repeat the process for the rest of word
            }

            // finally, print to standard output its rank number
            System.out.println(rank);

        }// end of the for-loop for each word from the command line

        // this line is for calculating the execution time
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time in ms: " + (endTime - startTime));

    }// end of main method

    //----------------- charArrToHashWithRank Method ----------------------
    // charArrToHashWithRank method returns a hashMap, which maps keys (chars)
    // to values (rank in alphabetical order within the word)
    // e.g. the word: HELLO
    //      hashtable keys:[L,H,E,O]
    //      hashtable values:[3,2,1,4]
    //---------------------------------------------------------------------
    private static HashMap<Character,Integer> charArrToHashWithRank(char[] charArr)
    {
        HashMap<Character,Integer> charHash = new HashMap<Character,Integer>();
        int valueAdder=1;

        for (int i=1; i<charArr.length;i++) {

            if (charArr[i] != charArr[i-1]) { // avoid storing repeated char
                charHash.put(new Character(charArr[i-1]), new Integer(valueAdder));
                valueAdder++;
            }
        }
        // storing the last char
        charHash.put(new Character(charArr[charArr.length-1]), new Integer(valueAdder));

        return charHash;
    }// end of charArrToHashRank method

    //----------------- getChar Method -------------------------------------
    // getChar method accepts a hashMap and a value, and then returns the
    // corresponding key (char) from the value (int).
    //---------------------------------------------------------------------
    private static char getChar(HashMap<Character,Integer> charHash, int num)
    {
        Object fch = new Object();
        Integer n = new Integer(num);
        for (Map.Entry entry: charHash.entrySet()) {
            if (n.equals(entry.getValue())) {
                fch = entry.getKey();
                break;
            }
        }

        return fch.toString().charAt(0);
    }// end of getChar method

    //----------------- permutWithRepeat Method --------------------------------
    // permutWithRepeat method accepts an array of chars, and then returns
    // the number of all the different arrangements with possible repeated items
    // i.e. result = (total_num_of_letters)!/(num_of_repeats)!
    //--------------------------------------------------------------------------
    private static BigInteger permutWithRepeat(char[] charArr)
    {
        int tempCounter=1;

        BigInteger denominator = BigInteger.ONE;
        BigInteger numerator, fnumOfRepeats;

        for (int i=1; i<charArr.length;i++) {

            if (charArr[i] == charArr[i-1]) { // counting repeats
                tempCounter++;
            } else {
                if (tempCounter != 1){
                    fnumOfRepeats = factorial(tempCounter);
                    denominator = denominator.multiply(fnumOfRepeats);
                }

                tempCounter=1;
            }
        }
        // multiply the factorial of the last number of repeated items
        fnumOfRepeats = factorial(tempCounter);
        denominator = denominator.multiply(fnumOfRepeats);

        numerator = factorial(charArr.length);

        return numerator.divide(denominator);
    }// end of permutWithRepeat method

    //----------------- factorial Method --------------------------------
    // factorial method returns the factorial of a number
    //-------------------------------------------------------------------
    private static BigInteger factorial(int num)
    {
        BigInteger result = BigInteger.ONE;
        for (int i=1; i<=num; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }

        return result;
    }// end of factorial method

}// end of WordRanker class
