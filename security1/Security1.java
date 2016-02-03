package security1;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Genevieve Suwara, Kevin Ripley
 */
public class Security1 {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //initialize variables
        String name;
        String number;
        String date;
        String cvc;
        LinkedList results;
        
        //set results to the matched strings found in getInfo
        results = getInfo();

        //set count to the number of matches found
        int count = results.size();

        System.out.println("\n" + count + " credit card record(s) found.\n");
        
        //iterate through results
        for (int i = 1; i <= results.size(); i++) {
            //extract info from result string
            String result = (String) results.get(i - 1);
            String[] fields = result.split("\\^");
            number = "";
            
            for(int j = 0; j < (fields[0].length() - 2); j++){
                number += fields[0].substring(j + 2,j+3);
                if(j % 4 == 3)
                {
                    number += " ";
                }
            }
            
            String[] fullName = fields[1].split("\\/");
            name = fullName[0] + " " + fullName[1];
            date = fields[2].substring(2, 4) + "/20" + fields[2].substring(0, 2);
            cvc = fields[2].substring(4, 7);

            //print result info
            System.out.println("<Information of record " + i + ">");
            System.out.println("Cardholder's Name: " + name);
            System.out.println("Card Number: " + number);
            System.out.println("Expiration Date: " + date);
            System.out.println("CVC Number: " + cvc + "\n");
        }
    }

    public static LinkedList getInfo() throws FileNotFoundException, IOException {
        byte[] data;
        data = new byte[200000000];

        try {
            //ask user for file input
            Scanner reader = new Scanner(System.in);
            System.out.println("Please enter file path with double backslashes\n(Ex. C:\\\\Users\\\\user\\\\Desktop\\\\memorydump.dmp):");
            String fileName = reader.next();
            //open file
            FileInputStream file = new FileInputStream(new File(fileName));
            //read bytes into data array
            file.read(data);
            //close file
            file.close();
        } catch (Exception e) {
        }

        LinkedList matches;
        
        //convert data into readable format
        String decodeString = new String(data, "UTF-8");

        Pattern pattern;

        //set pattern to be matched

        //We assumed here that the credit card number could be 13 to 19 digits 
        //long based on the Track 1 format from the slides. We also assumed that
        //both name fields would have to be at least one character long and no 
        //longer than 26 characters.  The "\\w*" near the end of the expression 
        //is what we used to read in additional data other than expiration and 
        //cvc assuming that it would only contain alphanumeric characters.
        pattern = Pattern.compile("\\%(B)\\d{13,19}\\^\\p{Alpha}{1,26}\\/\\p{Alpha}{1,26}\\^\\d{2}(0\\d|10|11|12)\\d{3}\\w*\\?");

        Matcher matcher;
        matcher = pattern.matcher(decodeString);
        matches = new LinkedList();

        //read regex matches into matches
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }

}
