package au.bzea.storepurchase.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class Currencies {
    
    private static Logger logger = Logger.getLogger(Currencies.class.getName());

    public static List<String> getCurrencies() throws IOException{
        // text file of currencies
        String fileName = "static/country_currency.txt";
        // list that holds strings of a file
        List<String> listOfStrings
            = new ArrayList<String>();
       
        // creating object of the class
        // important since the getClass() method is 
        // not static.
        Currencies obj = new Currencies();
        
    
        // declaring the input stream
        InputStream instr = obj.getClass().getClassLoader().getResourceAsStream(fileName);
  
        // reading the files with buffered reader 
        InputStreamReader strrd = new InputStreamReader(instr);
         
        BufferedReader rr = new BufferedReader(strrd);
  
        String line;
        
        // checking for end of file
        while ((line = rr.readLine()) != null) listOfStrings.add(line);
        logger.info("getCurrencies count: " + listOfStrings.size());

        return listOfStrings;
    }


}
