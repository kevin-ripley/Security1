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

        System.out.println(count + " credit card record(s) found.\n");
        
        //iterate through results
        for (int i = 1; i <= results.size(); i++) {
            //extract info from result string
            String result = (String) results.get(i - 1);
            String[] fields = result.split("\\^");

            number = fields[0].substring(2, 6) + " "
                    + fields[0].substring(6, 10) + " " + fields[0].substring(10, 14) + " " + fields[0].substring(14, 18) + " ";
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
            //open file
            FileInputStream file = new FileInputStream(new File("C:\\Users\\smart_000\\Documents\\memorydump(1).dmp"));
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
        pattern = Pattern.compile("\\%(B)\\d*\\^\\p{Alpha}*\\/\\p{Alpha}*\\^\\d*\\w*\\?");

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
