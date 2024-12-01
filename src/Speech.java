import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class represents an individual speech of a President.
 * It uses the Gunning Fog Index Algorithim to calculate how complex
 * the speech is, assigning it a complexity variable
 * Syllable Counter Algorithim inspired by Ramon Ascuncion's
 * syllable counter algorithim on Github
 * @author Yuyang Hu, Angelina Wu, Issac Zhang
 *
 */
public class Speech {
    private String file;
    private String presidentName;
    private double complexity;
    /**
     * Initalizes a new Speech object
     * @param file
     */
    public Speech(String file) {
        this.file = file;
        File f = new File(file);
        Scanner s = null;
        try{
            s = new Scanner(f);
        }catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        s.nextLine();
        s.nextLine();
        String name = s.nextLine();
        presidentName = name;
        this.complexity = complexity();
    }
    /**
     * Returns the name of the president making the speech
     * @return
     */
    public String getPresidentName(){
        return presidentName;
    }
    /**
     * Counts the numnber of syllables in a word
     * @param word
     * @return syllables
     */
    public double syllables(String word){
        String[] triphthongs = {"eau", "iou", "ieu"};
        String[] diphthongs = {"aa", "ai", "au", "ay", "ea", "ee", "ei", "eu", "ey",
                "ie", "io", "oa", "oe", "oi", "oo", "ou", "oy", "ue", "ui"};
        int syllables = 0;
        Pattern vowels = Pattern.compile("[aeiouy]+");
        Matcher vowelMatch = vowels.matcher(word);
        //Counts the number of vowels
        while (vowelMatch.find()) {
            syllables++;
        }
        //Disregards silent Es
        if(word.endsWith("e") || (word.length() > 1 && word.charAt(word.length() - 2) == 'e')){
            syllables--;
        }
        //Disregards Triphthongs
        for(String t : triphthongs){
            if(word.contains(t)){
                syllables--;
            }
        }
        //Disregards Diphthongs
        for(String d : diphthongs){
            if(word.contains(d)){
                syllables--;
            }
        }
        //Disregards Ys only if Ys are leaading
        if(word.contains("y") && !word.startsWith("y")){
            syllables++;
        }
        //Disregards common suffixes
        if(word.endsWith("es") || word.endsWith("ed") || word.endsWith("ing")){
            syllables--;
        }
        return syllables;
    }
    /**
     * Determines the complexity of a speech
     * @return complexity
     */
    public double complexity(){
        File f = new File(file);
        Scanner s = null;
        try{
            s = new Scanner(f);
        }catch(FileNotFoundException e){
            System.err.println("File not found");
        }
        String line = "";
        while(!line.equals("Transcript")){
            line = s.nextLine();
        }
        int words = 0;
        int sentences = 0;
        int complexWords = 0;
        while(s.hasNextLine()) {
            line = s.nextLine();
            Scanner lineScanner = new Scanner(line);
            while(lineScanner.hasNext()){
                String word = lineScanner.next();
                word = word.toLowerCase();
                if(word.contains(".") || word.contains("?") || word.contains("!")){
                    sentences++;
                }
                words++;
                if(syllables(word) >= 3){
                    complexWords++;
                }
            }
        }
        return 0.4 * ((words/sentences) + 100 * (complexWords/words));
    }
}
