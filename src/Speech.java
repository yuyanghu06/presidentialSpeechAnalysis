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
    private double complexity;
    /**
     * Initalizes a new Speech object
     * @param String file
     */
    public Speech(String file) {
        this.file = file;
        this.complexity = complexity();
    }
    /**
     * Determines the complexity of a speech
     * @return int complexity
     */
    public double complexity(){
        String[] triphthongs = {"eau", "iou", "ieu"};
        String[] diphthongs = {"aa", "ai", "au", "ay", "ea", "ee", "ei", "eu", "ey",
                        "ie", "io", "oa", "oe", "oi", "oo", "ou", "oy", "ue", "ui"};
        Scanner s = new Scanner(file);
        int words = 0;
        int sentences = 0;
        int complexWords = 0;
        while(s.hasNextLine()) {
            String line = s.nextLine();
            Scanner lineScanner = new Scanner(line);
            while(lineScanner.hasNext()){
                int syllables = 0;
                String word = lineScanner.next();
                word = word.toLowerCase();
                if(word.contains(".")){
                    sentences++;
                }
                words++;
                Pattern vowels = Pattern.compile("[aeiouy]+");
                Matcher vowelMatch = vowels.matcher(word);
                //Counts the number of vowels
                while (vowelMatch.find()) {
                    syllables++;
                }
                //Disregards silent Es
                if(word.endsWith("e") || word.charAt(word.length() - 2) == 'e'){
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
                if(syllables >= 3){
                    complexWords++;
                }
            }
        }
        return 0.4 * ((words/sentences) + 100 * (complexWords/words));
    }
}
