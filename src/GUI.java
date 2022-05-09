//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.border.EmptyBorder;
//import java.util.Scanner;
//import javax.xml.transform.Templates;

import java.io.*;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
import java.util.*;

public class GUI extends JFrame implements ActionListener {

    private static JPanel panel;
    private static JFrame frame;

    private static JLabel Title;
    private static JLabel stats;
    private static JTextField userText1;
    private static JLabel[] labels;

    public static Scanner s = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    static String[] possibleWords;
    static int tries;
    static char[] input;
    static long startTime;
    static char[] answer;
    static boolean done;
    static String answerChoosen;






    //-------------------------------------------------------------------
	public static void main(String[] args) throws IOException {
// saziimet laukumu no sakuma
//izveidot frame
        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("LINGO");
        frame.setLocationRelativeTo(null);
        frame.add(panel);
//vaards
        panel.setLayout(null);
        Title = new JLabel("Ieraksti vārdu: ");
        Title.setBounds(10, 20, 150, 25);
        panel.add(Title);
//ieraksti vardu
        panel.setLayout(null);
        stats = new JLabel(" ");
        stats.setBounds(10, 50, 180, 25);
        panel.add(stats);
// texta field
        userText1 = new JTextField();
        userText1.addActionListener(new GUI());
        userText1.setBounds(40, 80 + (0 * 25), 80, 25);
        panel.add(userText1);
//poga enter
        JButton button = new JButton("Enter");
        button.setBounds(200, 20, 80, 25);
        button.addActionListener(new GUI());
        panel.add(button);

        

        
// uzziimee tukshas vietas
        labels = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            labels[i] = new JLabel("<html><font size='5' color=blue> ----- </font> <font");
            labels[i].setBounds(44, 80 + (i * 25), 80, 25);
            panel.add(labels[i]);
        }

        frame.setVisible(true);
        
        StartWordle(); //sakt speeli
        
    }

    //--------------------------------------------------------------------------------------------------------------------

    public static void StartWordle() throws IOException {
        Playing.is = true;/*
        //izveido masivu ar iespejamajiem vardiem, kurus var ierakstit
        possibleWords = new String[12947];
        try { 
            File myObj = new File("src\\wordleWords.txt");
            Scanner myReader = new Scanner(myObj);
            int indexCounter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //add data to the array
                possibleWords[indexCounter] = data;
                indexCounter++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }*/
        GetFile importer = new GetFile();
        possibleWords = importer.importFile("src\\wordleWords.txt");

//uznem laiku kops sakshanas
        startTime = System.currentTimeMillis();
        tries = 0;
        //Tiek izvelets random vards no iespejamajam atbildem, kaa ari uztaisit burtu masiivs
        answerChoosen = ReturnRandomWord();
        answer = new char[5];
        for (int i = 0; i < 5; i++ ) answer[i] = answerChoosen.charAt(i);

        input = new char[5];
    }
//beidzas start lingo
//-------------------------------------------------------------------------

    





    public static void EndWordle() {
        Playing.is = false;
        System.out.println("Vārds bija: " + new String(answerChoosen));
        System.out.println("Atbilde atrasta: " + ((System.currentTimeMillis() - startTime) / 1000) + " sekundēs un " + tries + " mēģinājumos.");

        userText1.setEnabled(false);
        userText1.setVisible(false);

        if (!done) stats.setText("<html><font size='2' color=red> " + "Atbilde bija: " + new String(answerChoosen) + ". Tu izniekoji \n " + ((System.currentTimeMillis() - startTime) / 1000) + " sekundes ):" + "</font> <font");
        else  stats.setText("<html><font size='2' color=green> " + "Atbilde atrasta \n " + ((System.currentTimeMillis() - startTime) / 1000) + " sekundēs un " + tries + " piegājienos." + "</font> <font");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(Playing.is);
        //Auto-generated method stub // if the button is pressed
        if (!Playing.is) {

            try {
                StartWordle();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }else{
            EnterWord();
        }
        
        
        
    }

    public static void EnterWord(){ //if its good, actually submit the word for checking
        if ( IsAValidWord(userText1.getText(), possibleWords) ) ButtonPressed();
        else System.out.println("Wordle: That is not a valid word");
    }





    public static void ButtonPressed(){
        //parvietot input lauku zemak ar katru meginjajumu
        userText1.setBounds(40, 80 + ((tries + 1) * 25), 80, 25);

        String userInput = userText1.getText();
        int[] colorOfLetters = PlayWordle(userInput);
//salidzina, ja visas burtu krasas ir zalas, tad beigt speli
        done = true;
        for (int i : colorOfLetters) {
            if (i != 2) done = false;
        }
        if (done || tries > 5) EndWordle();

        String[] numsToColors = new String[5];
        for (int i = 0; i < 5; i++) {
            if (colorOfLetters[i] == 0) numsToColors[i] = "black";
            else if (colorOfLetters[i] == 1) numsToColors[i] = "orange";
            else if (colorOfLetters[i] == 2) numsToColors[i] = "green";
        }

        System.out.println("Set colors to " + numsToColors[0] + " " + numsToColors[1] + " " + numsToColors[2] + " " + numsToColors[3] + " " + numsToColors[4] + " User Input was" + userInput + " answer was " + answerChoosen + " work on word is " + new String(answer));
        String finalString = (
        "<html><font size='5' color=" + numsToColors[0] + "> " + userInput.charAt(0) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[1] + "> " + userInput.charAt(1) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[2] + "> " + userInput.charAt(2) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[3] + "> " + userInput.charAt(3) + "</font> <font            " + 
        "<html><font size='5' color=" + numsToColors[4] + "> " + userInput.charAt(4) + "</font> <font            ");
        setNextLabel(finalString);

        userText1.setText(""); //set the text box to "" after all the logic is done
    }








    
    public static int[] PlayWordle(String InputWordleWord) {
        done = false;
        tries++;

        String R1 = InputWordleWord.toLowerCase();//String R1 = s.nextLine().toLowerCase();

        //check if it is 5 letters and is a possible word
        if (!IsAValidWord(R1, possibleWords)) {
            System.out.println("wasnt a good word");
        } else {
            for (int i = 0; i < 5; i++ ) { //puts the inputWord into a char[]
                input[i] = R1.charAt(i);
            }
        }
//just reset answer every time
        for (int i = 0; i < 5; i++ ) answer[i] = answerChoosen.charAt(i);
        return ReturnColorOfLeters(input, answer);
    }

    public static void setNextLabel(String string){
        labels[tries - 1].setText(string);
    }

    public static int[] ReturnColorOfLeters(char[] inputWord, char[] correctWord) {
        char[] answerTemp = correctWord;
        int[] colorForLetter = new int[5]; //0 is grey, yellow is 1, green is 2

        for (int i = 0; i < 5; i++) { //check for any correct position+letter (green)
            if (inputWord[i] == answerTemp[i]) {
                answerTemp[i] = '-';
                colorForLetter[i] = 2;
            }
        }

        for (int j = 0; j < 5; j++) { //check for any correct letter (yellow)
            for (int k = 0; k < 5; k++){
                if (inputWord[j] == answerTemp[k] && colorForLetter[j] != 2) {
                    //if that letter is not already green and matches some other letter
                    colorForLetter[j] = 1;
                    answerTemp[k] = '-';
                }
            }
        }

        for (int m = 0; m < 5; m++) {
            if (colorForLetter[m] == 0) System.out.print(inputWord[m]);
            if (colorForLetter[m] == 1) System.out.print(ANSI_YELLOW + inputWord[m] + ANSI_RESET);
            if (colorForLetter[m] == 2) System.out.print(ANSI_GREEN + inputWord[m] + ANSI_RESET);
        }

        System.out.println("");
        return colorForLetter;
    }

    public static boolean IsAValidWord(String input, String[] possibleWords) {
        if (input.length() < 5) {
            System.out.println("Wordle: The Word You Entered Was Not Long Enough");
            return false;
        }
        for (String string : possibleWords) {
            if (string.equals(input)) {
                return true;
            }
        }
        return false;
    }

    public static String ReturnRandomWord() throws IOException{
        /*
        String[] answerList = new String[2315];
        try { 
            File myObj = new File("src\\wordleAnswers.txt");
            Scanner myReader = new Scanner(myObj);
            int indexCounter = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //add data to the array
                answerList[indexCounter] = data;
                indexCounter++;
            }
            myReader.close();   
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        */
        String[] answerList = new String[2315];
        GetFile importer = new GetFile();
        answerList = importer.importFile("src\\wordleAnswers.txt");

        return answerList[(int)(Math.random() * (answerList.length - 1))]; //returns a random word from this large list
    }
}
