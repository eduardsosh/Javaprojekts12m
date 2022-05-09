import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GetFile {
    public String [] importFile(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();


        String[] words = new String[lines];
        //words = new String[];
        try { 
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            int indexCounter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //add data to the array
                words[indexCounter] = data;
                indexCounter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();





        
        
        
        
    }return words;
    }}
